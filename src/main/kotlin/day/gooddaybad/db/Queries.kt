package day.gooddaybad.db

fun Db.person(password: String) = one(
    Person::class, """
    for person in @@collection
        filter person.${f(Person::password)} == @password
        return person
    """, mapOf(
        "password" to password
    )
)

val Db.inactivePeople get() = list(
    Person::class, """
        for person in @@collection
            filter person.${f(Person::active)} != true
            sort date_timestamp(person.${f(Person::updated)})
            return person
    """.trimIndent()
)

fun Db.me(password: String) = one(
    Person::class, """
        for person in @@collection
            filter person.${f(Person::password)} == @password
            return person
    """.trimIndent(),
    mapOf("password" to password)
)

val Db.activePromptPacks get() = list(
    PromptPack::class, """
        for promptPack in @@collection
            filter promptPack.${f(PromptPack::active)} == true
            sort date_timestamp(promptPack.${f(PromptPack::updated)}) desc
            return promptPack
    """.trimIndent()
)

fun Db.allPacks(person: String) = list(
    PromptPack::class, """
        for promptPack in @@collection
            filter promptPack.${f(PromptPack::person)} == @person
            sort date_timestamp(promptPack.${f(PromptPack::updated)}) desc
            return promptPack
    """.trimIndent(),
    mapOf("person" to person.asKey())
)

val Db.countPeople
    get() = query(
        Int::class, """
    return count(`${Person::class.collection}`)
    """
).first()!!
