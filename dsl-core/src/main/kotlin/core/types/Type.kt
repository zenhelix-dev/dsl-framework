package core.types

/**
 * Базовый интерфейс типа
 */
interface Type {
    val name: String
    val qualifiedName: String
    val typeParameters: List<TypeParameter>
    fun isAssignableFrom(other: Type): Boolean
}

/**
 * Параметр типа (дженерик)
 */
data class TypeParameter(
    val name: String,
    val constraints: List<TypeConstraint> = emptyList()
)

/**
 * Ограничения типов
 */
sealed class TypeConstraint {
    data class UpperBound(val type: Type) : TypeConstraint()
    data class LowerBound(val type: Type) : TypeConstraint()
}

/**
 * Простой тип (без дженериков)
 */
class SimpleType(
    override val name: String,
    override val qualifiedName: String = name,
    override val typeParameters: List<TypeParameter> = emptyList()
) : Type {
    override fun isAssignableFrom(other: Type): Boolean {
        return this == other || other is NullableType && other.baseType == this
    }

    override fun equals(other: Any?): Boolean {
        return other is SimpleType && qualifiedName == other.qualifiedName
    }

    override fun hashCode(): Int = qualifiedName.hashCode()

    override fun toString(): String = qualifiedName
}

/**
 * Дженерик тип
 */
class GenericType(
    val baseType: Type,
    val typeArguments: List<Type>
) : Type {
    override val name: String = baseType.name
    override val qualifiedName: String = "${baseType.qualifiedName}<${typeArguments.joinToString(", ") { it.name }}>"
    override val typeParameters: List<TypeParameter> = emptyList()

    override fun isAssignableFrom(other: Type): Boolean {
        return when (other) {
            is GenericType -> baseType == other.baseType &&
                    typeArguments.zip(other.typeArguments).all { (a, b) -> a.isAssignableFrom(b) }

            else -> false
        }
    }

    override fun toString(): String = qualifiedName
}

/**
 * Nullable тип
 */
class NullableType(
    val baseType: Type
) : Type {
    override val name: String = "${baseType.name}?"
    override val qualifiedName: String = "${baseType.qualifiedName}?"
    override val typeParameters: List<TypeParameter> = baseType.typeParameters

    override fun isAssignableFrom(other: Type): Boolean {
        return when (other) {
            is NullableType -> baseType.isAssignableFrom(other.baseType)
            else -> baseType.isAssignableFrom(other)
        }
    }

    override fun toString(): String = qualifiedName
}