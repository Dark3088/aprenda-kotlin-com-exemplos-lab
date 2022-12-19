import DIOEducationalContent.getSingleContentCollection
import DIOEducationalContent.retrieveAllEducationalContentAsList

const val ANDROID_DEVELOPER = 0
const val FRONTEND_DEVELOPER = 1
const val UNITY3D_DEVELOPER = 2
enum class ContentLevel { BASIC, INTERMEDIATE, ADVANCED }

class AndroidCareer(
    careerName: String = "Android Developer",
    content: MutableList<EducationalContent>? = null
) : Career(
    careerName,
    content
)

open class Career(
     var careerName: String? = "",
     var content: MutableList<EducationalContent>? = null
) {
    // TODO: Implement this property
    val currentStack: List<String>? get() = content?.map { it.toString() }

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

    // TODO: Implement these properties
     val mContentLevel: String get() = when(contentLevel) {
        ContentLevel.BASIC -> "Basic"
        ContentLevel.INTERMEDIATE -> "Intermediate"
        ContentLevel.ADVANCED -> "Advanced"
        else -> ""
    }
    
    val mContentDuration: String get() = "${ contentDuration / 60 } hour(s)"
}

// A User only needs a name to get it started
data class User(val name: String, var userEducation: MutableList<Career>? = null)

// Enrolled Careers can be accessed by extension property and by extension function
val User.enrolledContent: String get() = userEducation?.joinToString { it.careerName!! }.toString()
//fun User.getStudentFormations(): String = userEducation?.joinToString { it.careerName!! }.toString()

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

    fun getContentKeys() : List<String> {
        val contentKeys = arrayListOf<String>()

        educationalContent.onEach { item ->
            contentKeys.add(item.key)
        }
        return contentKeys
    }

    fun retrieveAllEducationalContentAsList(): List<String> {
        val allEducationalContent = ArrayList<String>()

        educationalContent.map { entry ->
            entry.value.map { educationalContent ->
                allEducationalContent.add(educationalContent.contentName)
            }
        }
        return allEducationalContent
    }

    fun getSingleContentCollection(contentKey: String) : MutableList<EducationalContent> {
        try {
            return educationalContent[contentKey]!!

        } catch (e: NullPointerException) {
            println("${e.message} : Educational Content could not be found. Did you enter the correct \"contentKey\"?")
        }
        return emptyList<EducationalContent>().toMutableList()
    }
}

fun main() {

    val contentKeys = DIOEducationalContent.getContentKeys()
    val androidDevelopment  = getSingleContentCollection(contentKeys[ANDROID_DEVELOPER])
    val frontEndDevelopment  = getSingleContentCollection(contentKeys[FRONTEND_DEVELOPER])
    val unity3dDeveloper = getSingleContentCollection(contentKeys[UNITY3D_DEVELOPER])

    /*  TEST CASES */

    // A single Career
    var currentCareer = registerCareerAs(contentKeys[ANDROID_DEVELOPER], AndroidCareer())
    // currentCareer = registerCareerAs(contentKeys[ANDROID_DEVELOPER], Career())       // "AndroidCareer" can be
                                                                                       // replaced with "Career"

    // There can be many careers available
    val availableCareers = mutableListOf(
        Career(careerName = getCareerNameBy(contentKeys[ANDROID_DEVELOPER]), content = androidDevelopment),
        Career(careerName = getCareerNameBy(contentKeys[FRONTEND_DEVELOPER]), content = frontEndDevelopment),
        Career(careerName = getCareerNameBy(contentKeys[UNITY3D_DEVELOPER]), content = unity3dDeveloper)
    )

    val student = User(name = "Diego")                      // A user that hasn't enrolled in any career
     //  .onlyStudies(currentCareer)                       // Enrolled in a single formation
     student.onlyStudies(currentCareer)                   // You can also call on the instance

   // student.studies(availableCareers)                  // Enrolled in several careers

    // Which careers the student is currently enrolled in
    println("${student.name} is enrolled in: ${student.enrolledContent}" )

    val allContent = retrieveAllEducationalContentAsList().joinToString { it }
   // println( "A list of All content available:\n$allContent")     // All Educational Content available
}

private fun registerCareerAs(careerKey: String, career: Career): Career{

    val educationalContent = DIOEducationalContent.getSingleContentCollection(careerKey)
    return career.apply {
        careerName = getCareerNameBy(careerKey)
        content = educationalContent
    }
}

private fun getCareerNameBy(key: String) : String{
    return when (key){
        "android_developer" -> "Android Developer"
        "front_end_developer" -> "Front-End Developer"
        "unity_3d_game_developer" -> "Unity 3D Game Developer"
        else -> "NO EDUCATION"
    }
}

private fun User.onlyStudies(singleFormation: Career): User{

    return apply {
        this.userEducation = mutableListOf(singleFormation)
        singleFormation.receives(this)
    }
}

private fun User.studies(formation: MutableList<Career>) : User{

    return apply {
        this.userEducation = formation

        formation.forEach { enrolledFormation ->
            enrolledFormation.receives(this)
        }
    }
}

