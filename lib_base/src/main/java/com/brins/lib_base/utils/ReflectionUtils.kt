package com.brins.lib_base.utils

import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

object ReflectionUtils {

    // Create an instance of a class
    fun <T : Any> createInstance(klass: KClass<T>): T? {
        val noArgConstructor = klass.primaryConstructor ?: klass.constructors.firstOrNull { it.parameters.isEmpty() }
        noArgConstructor?.let {
            it.isAccessible = true
            return it.call()
        }
        return null
    }

    /**
     * Creates an instance of the specified class using the constructor that matches the given arguments.
     * @param klass The KClass of the class to instantiate.
     * @param args The arguments to pass to the constructor.
     * @return The created instance.
     */
    fun <T : Any> createInstance(klass: KClass<T>, vararg args: Any?): T {
        val constructor = klass.constructors.firstOrNull { constructor ->
            constructor.parameters.size == args.size && constructor.parameters.map { it.type.classifier }.zip(args).all { (classifier, arg) ->
                arg == null || classifier == arg::class || (arg as? Class<*>)?.isAssignableFrom(classifier as Class<*>) ?: false
            }
        }

        return constructor?.call(*args)
            ?: throw IllegalArgumentException("No suitable constructor found for class ${klass.qualifiedName} with parameters: ${args.joinToString()}")
    }

    /**
     * Creates an instance of a class using its fully qualified name.
     * @param className The fully qualified name of the class.
     * @param args The arguments to be passed to the constructor, if any.
     * @return An instance of the specified class, or null if the instance cannot be created.
     */
    fun createInstance(className: String, vararg args: Any?): Any? {
        return try {
            val clazz = Class.forName(className).kotlin
            val constructor = clazz.constructors.firstOrNull { constructor ->
                constructor.parameters.size == args.size &&
                        constructor.parameters.map { it.type.classifier as? KClass<*> }
                            .zip(args).all { (paramClass, arg) -> arg == null || paramClass?.isInstance(arg) == true }
            }

            constructor?.call(*args)
        } catch (e: ClassNotFoundException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    // Get the value of a property
    fun <T: Any, R> getProperty(instance: T, propertyName: String): R? {
        val property = instance::class.memberProperties
            .firstOrNull { it.name == propertyName }
            ?: throw NoSuchElementException("No property named $propertyName found in ${instance::class.qualifiedName}")
        property.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return property.getter.call(instance) as? R
    }

    // Set the value of a mutable property
    fun <T : Any> setProperty(instance: T, propertyName: String, value: Any?) {
        val property = instance::class.memberProperties
            .firstOrNull { it.name == propertyName } as? KMutableProperty<*>
            ?: throw NoSuchElementException("No mutable property named $propertyName found")
        property.isAccessible = true
        property.setter.call(instance, value)
    }



    // Call a method on an object
    fun callMethod(instance: Any, methodName: String, vararg args: Any?): Any? {
        val method = instance::class.functions.firstOrNull { it.name == methodName && it.parameters.size == args.size + 1 }
            ?: throw NoSuchElementException("No method named $methodName found with ${args.size} arguments")
        method.isAccessible = true
        return method.call(instance, *args)
    }

    // Get all methods of a class
    fun getAllMethods(klass: KClass<*>): Collection<KFunction<*>> {
        return klass.memberFunctions + klass.staticFunctions
    }

    // Get all properties of a class
    fun getAllProperties(klass: KClass<*>): Collection<KProperty1<*, *>> {
        // 获取所有成员属性
        val memberProperties: Collection<KProperty1<*, *>> = klass.memberProperties

        // 尝试获取伴随对象的属性
        val companionProperties: Collection<KProperty1<*, *>> = klass.companionObject?.memberProperties ?: emptyList()

        // 返回合并后的属性列表
        return memberProperties + companionProperties
    }

    // Get and set static properties (properties defined in a companion object)

    /*fun getStaticProperty(klass: KClass<*>, propertyName: String): Any? {
        // 获取伴随对象
        val companionObject = klass.companionObject
            ?: throw NoSuchElementException("No companion object found for class ${klass.qualifiedName}")

        // 获取伴随对象的实例
        val companionObjectInstance = companionObject.objectInstance
            ?: throw IllegalStateException("Companion object has no instance in class ${klass.qualifiedName}")

        // 获取属性并设置为可访问
        val property = companionObject.memberProperties
            .firstOrNull { it.name == propertyName }
            ?: throw NoSuchElementException("No companion object property named $propertyName found")
        property.isAccessible = true

        // 返回属性值
        return property.get(companionObjectInstance)
    }*/


    fun setStaticProperty(klass: KClass<*>, propertyName: String, value: Any?) {
        val companion = klass.companionObject ?: throw NoSuchElementException("No companion object found for class ${klass.qualifiedName}")
        val property = companion.memberProperties.firstOrNull { it.name == propertyName } as? KMutableProperty<*>
            ?: throw NoSuchElementException("No mutable companion object property named $propertyName found")
        property.isAccessible = true
        property.setter.call(klass.objectInstance, value)
    }

    fun <T: Any> setStaticProperty(instance: T, propertyName: String, value: Any?) {
        val klass = instance::class
        val companionObj = klass.companionObject
            ?: throw NoSuchElementException("No companion object found for class ${klass.qualifiedName}")

        // 获取伴随对象的实例
        val companionObjInstance = companionObj.objectInstance
            ?: throw IllegalStateException("Companion object has no accessible instance")

        // 获取属性并设置为可访问
        val property = companionObj.memberProperties
            .firstOrNull { it.name == propertyName } as? KMutableProperty1<Any, Any?>
            ?: throw NoSuchElementException("Mutable property '$propertyName' is not found in ${klass.qualifiedName}")

        // 修改静态属性的值
        property.isAccessible = true
        property.set(companionObjInstance, value)
    }

    // Call static methods (methods defined in a companion object)
    fun callStaticMethod(klass: KClass<*>, methodName: String, vararg args: Any?): Any? {
        val companion = klass.companionObject ?: throw NoSuchElementException("No companion object found for class ${klass.qualifiedName}")
        val method = companion.memberFunctions.firstOrNull { it.name == methodName && it.parameters.size == args.size + 1 }
            ?: throw NoSuchElementException("No companion object method named $methodName found with ${args.size} arguments")
        method.isAccessible = true
        return method.call(klass.objectInstance, *args)
    }
}



