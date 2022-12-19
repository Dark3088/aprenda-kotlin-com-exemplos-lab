
enum class ContentLevel { BASIC, INTERMEDIATE, ADVANCED }

data class Formation(
    val formationName: String,
    var content: MutableList<EducationalContent>
) {

    private val educationalContentList: List<String>
        get() = content.map { it.toString() }

    private val enrolledStudents = mutableListOf<User>()

    fun receives(vararg user: User) {
        for (newStudent in user)
            enrolledStudents.add(element = newStudent)
    }
}

// A class representing some Educational Content
data class EducationalContent(
    val contentName: String,
    val contentLevel: ContentLevel,
    val contentDuration: Int
) {

    private val mContentLevel: String get() = when(contentLevel) {
        ContentLevel.BASIC -> "Basic"
        ContentLevel.INTERMEDIATE -> "Intermediate"
        ContentLevel.ADVANCED -> "Advanced"
        else -> ""
    }
}

// A User only needs a name to get it started
data class User(val name: String, var userFormation: MutableList<Formation>? = null)

// Enrolled Formations can be accessed by extension property and by extension function
val User.allFormations: String get() = userFormation?.joinToString { it.formationName }.toString()
//fun User.getStudentFormations(): String = userFormation?.joinToString { it.formationName }.toString()

//
object DIOEducationalContent {
    private val educationalContent = mapOf(
        "android_developer" to mutableListOf(
            EducationalContent("Kotlin", ContentLevel.INTERMEDIATE, 120),
            EducationalContent("GIT", ContentLevel.BASIC, 60),
            EducationalContent("Jetpack Compose", ContentLevel.BASIC, 60),
            EducationalContent("Dependency Injection", ContentLevel.ADVANCED, 180),
            EducationalContent("Tests", ContentLevel.ADVANCED, 180)),

        "front_end_developer" to mutableListOf(
            EducationalContent("Javascript", ContentLevel.INTERMEDIATE, 120),
            EducationalContent("GIT", ContentLevel.INTERMEDIATE, 180),
            EducationalContent("CSS", ContentLevel.BASIC, 60),
            EducationalContent("HTML", ContentLevel.BASIC, 60)),

        "unity_3d_game_developer" to mutableListOf(
            EducationalContent("Scripts in Unity", ContentLevel.INTERMEDIATE, 180),
            EducationalContent("Shaders", ContentLevel.ADVANCED, 120),
            EducationalContent("Scenes and 3D Space", ContentLevel.ADVANCED, 60),
        )
    )

    fun retrieveAllEducationalContent() : MutableList<EducationalContent> {
        return educationalContent.get(key = "android_developer") !!
    }

    fun getSingleContentCollection(contentKey: String) : MutableList<EducationalContent> {
        try {
            return educationalContent[contentKey]!!
        } catch (e: NullPointerException) {
            println("${e.message} : Educational Content could not be found. Did you type the \"contentKey\" correctly?")
        }
        return emptyList<EducationalContent>().toMutableList()
    }
}

fun main() {

    val androidDevelopment  = mutableListOf(
        EducationalContent("Kotlin", ContentLevel.INTERMEDIATE, 120),
        EducationalContent("GIT", ContentLevel.BASIC, 60),
        EducationalContent("Tests", ContentLevel.ADVANCED, 180),
    )

    // There can be many formations available
    val formations = mutableListOf(
        Formation(formationName = "Android", content = androidDevelopment),
        Formation(formationName = "Web Developer", content = androidDevelopment)
    )

    // Fail safe - Single Formation
    val educationalContent = DIOEducationalContent.getSingleContentCollection("android_developer")
    val androidFormation = Formation(formationName = "Android Developer", content = educationalContent)

    // Test Cases
    // A user that is not enrolled in any formation
    val student = User(name = "Diego")
    //   .onlyStudies(androidFormation)           // Enrolled in a single formation
    // student.onlyStudies(androidFormation)       // You can also call on the instance

    // Enrolled in several formations
    student.studies(formations)

    // What formations the student is currently enrolled in
    println("${student.name} is enrolled in: ${student.allFormations}" )

}

fun User.onlyStudies(singleFormation: Formation): User{

    return apply {
        this.userFormation = mutableListOf(singleFormation)
        singleFormation.receives(this)
    }
}

fun User.studies(formation: MutableList<Formation>) : User{

    return apply {
        this.userFormation = formation

        formation.forEach { enrolledFormation ->
            enrolledFormation.receives(this)
        }
    }
}

