package core.types

/**
 * Интерфейс типовой системы
 */
interface TypeSystem {
    fun resolveType(name: String): Type?
    fun getCommonSupertype(types: List<Type>): Type
    fun isSubtype(subtype: Type, supertype: Type): Boolean
    fun registerType(type: Type)
}

/**
 * Базовая реализация типовой системы
 */
class BaseTypeSystem : TypeSystem {
    private val types = mutableMapOf<String, Type>()

    init {
        // Базовые типы
        registerType(SimpleType("Any", "kotlin.Any"))
        registerType(SimpleType("Nothing", "kotlin.Nothing"))
        registerType(SimpleType("Unit", "kotlin.Unit"))
        registerType(SimpleType("String", "kotlin.String"))
        registerType(SimpleType("Int", "kotlin.Int"))
        registerType(SimpleType("Boolean", "kotlin.Boolean"))
        registerType(SimpleType("Long", "kotlin.Long"))
        registerType(SimpleType("Double", "kotlin.Double"))
        registerType(SimpleType("Float", "kotlin.Float"))
    }

    override fun resolveType(name: String): Type? {
        return types[name]
    }

    override fun getCommonSupertype(types: List<Type>): Type {
        if (types.isEmpty()) return resolveType("Nothing")!!
        if (types.size == 1) return types.first()

        // Упрощенная логика - возвращаем Any для всех типов
        return resolveType("Any")!!
    }

    override fun isSubtype(subtype: Type, supertype: Type): Boolean {
        return supertype.isAssignableFrom(subtype)
    }

    override fun registerType(type: Type) {
        types[type.name] = type
        if (type.name != type.qualifiedName) {
            types[type.qualifiedName] = type
        }
    }
}