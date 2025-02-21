package ee.carlrobert.codegpt.settings

import com.intellij.openapi.project.Project
import git4idea.GitUtil
import git4idea.branch.GitBranchUtil
import java.time.LocalDate

enum class Placeholder(val description: String, val code: String) {
    DATE_ISO_8601("Current date in ISO 8601 format, e.g. 2021-01-01.", "$" + "DATE_ISO_8601"),
    BRANCH_NAME("The name of the current branch.", "$" + "BRANCH_NAME"),
    GIT_DIFF(
        "The unified diff output showing uncommitted changes in the current Git working directory, including staged and unstaged modifications.",
        "$" + "GIT_DIFF"
    ),
    OPEN_FILES(
        "The complete source code contents of all files currently open in the IDE editor tabs, maintaining their formatting and structure.",
        "$" + "OPEN_FILES"
    ),
    ACTIVE_CONVERSATION("The complete conversation history with the AI assistant, including the most recent response and any relevant context from the current interaction.", "$" + "ACTIVE_CONVERSATION"),
    PREFIX("Code before the cursor.", "$" + "PREFIX"),
    SUFFIX("Code after the cursor.", "$" + "SUFFIX"),
    FIM_PROMPT(
        "Prebuilt Fill-In-The-Middle (FIM) prompt using the specified template.",
        "$" + "FIM_PROMPT"
    ),
}

interface PlaceholderStrategy {
    fun getReplacementValue(): String
}

class DatePlaceholderStrategy : PlaceholderStrategy {
    override fun getReplacementValue(): String {
        return LocalDate.now().toString()
    }
}

class BranchNamePlaceholderStrategy(val project: Project) : PlaceholderStrategy {
    override fun getReplacementValue(): String {
        return try {
            val repositories = GitUtil.getRepositoryManager(project).repositories
            if (repositories.isEmpty() || repositories.size != 1) {
                return "BRANCH-UNKNOWN"
            }

            GitBranchUtil.getBranchNameOrRev(repositories[0])
        } catch (ignore: Exception) {
            "BRANCH-UNKNOWN"
        }
    }
}