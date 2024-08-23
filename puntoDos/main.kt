import java.util.Scanner

// Clase base para empleados y clientes
open class Persona(
    var nombre: String,
    var documentoIdentidad: String,
    var sexo: String,
    var correoElectronico: String
)

// Clase para Empleados que hereda de Persona
class Empleado(
    nombre: String,
    documentoIdentidad: String,
    sexo: String,
    correoElectronico: String,
    var salario: Double,
    var dependencia: Dependencia,
    var añoIngreso: Int,
    var cargo: Cargo
) : Persona(nombre, documentoIdentidad, sexo, correoElectronico)

// Clase para Clientes que hereda de Persona
class Cliente(
    nombre: String,
    documentoIdentidad: String,
    sexo: String,
    correoElectronico: String,
    var direccion: String,
    var telefono: String
) : Persona(nombre, documentoIdentidad, sexo, correoElectronico)

// Clase para Cargo
class Cargo(
    var nombre: String,
    var nivelJerarquico: Int
)

// Clase para Dependencia
class Dependencia(
    var nombre: String
)

// Clase para Empresa
class Empresa(
    var razonSocial: String,
    var nit: String,
    var direccion: String,
    var empleados: MutableList<Empleado> = mutableListOf(),
    var clientes: MutableList<Cliente> = mutableListOf()
) {
    // Método para obtener el valor de la nómina
    fun calcularNomina(): Double {
        return empleados.sumOf { it.salario }
    }

    // Método para obtener la nómina por dependencia
    fun calcularNominaPorDependencia(dependencia: Dependencia): Double {
        return empleados.filter { it.dependencia.nombre == dependencia.nombre }.sumOf { it.salario }
    }

    // Método para obtener el porcentaje de clientes por sexo
    fun porcentajeClientesPorSexo(sexo: String): Double {
        val totalClientes = clientes.size
        val clientesPorSexo = clientes.count { it.sexo == sexo }
        return if (totalClientes > 0) (clientesPorSexo.toDouble() / totalClientes) * 100 else 0.0
    }

    // Método para mostrar porcentajes de clientes por los tres sexos
    fun mostrarPorcentajesDeClientesPorSexo() {
        val totalClientes = clientes.size
        val porcentajeMasculino = porcentajeClientesPorSexo("Masculino")
        val porcentajeFemenino = porcentajeClientesPorSexo("Femenino")
        val porcentajeOtros = porcentajeClientesPorSexo("Otros")

        println("Porcentaje de clientes por sexo:")
        println("Masculino: $porcentajeMasculino%")
        println("Femenino: $porcentajeFemenino%")
        println("Otros: $porcentajeOtros%")
        println("Total de clientes: $totalClientes")
    }

    // Método para obtener la cantidad de empleados según el nombre del cargo
    fun cantidadEmpleadosPorCargo(nombreCargo: String): Int {
        return empleados.count { it.cargo.nombre == nombreCargo }
    }

    // Método para obtener el empleado con más tiempo en la empresa
    fun empleadoConMasTiempo(): Empleado? {
        return empleados.minByOrNull { it.añoIngreso }
    }

    // CRUD para empleados
    fun añadirEmpleado(empleado: Empleado) {
        empleados.add(empleado)
    }

    fun eliminarEmpleado(documentoIdentidad: String) {
        empleados.removeIf { it.documentoIdentidad == documentoIdentidad }
    }

    fun actualizarEmpleado(documentoIdentidad: String, nuevoEmpleado: Empleado) {
        val index = empleados.indexOfFirst { it.documentoIdentidad == documentoIdentidad }
        if (index != -1) {
            empleados[index] = nuevoEmpleado
        }
    }

    fun buscarEmpleado(documentoIdentidad: String): Empleado? {
        return empleados.find { it.documentoIdentidad == documentoIdentidad }
    }

    // CRUD para clientes
    fun añadirCliente(cliente: Cliente) {
        clientes.add(cliente)
    }

    fun eliminarCliente(documentoIdentidad: String) {
        clientes.removeIf { it.documentoIdentidad == documentoIdentidad }
    }

    fun actualizarCliente(documentoIdentidad: String, nuevoCliente: Cliente) {
        val index = clientes.indexOfFirst { it.documentoIdentidad == documentoIdentidad }
        if (index != -1) {
            clientes[index] = nuevoCliente
        }
    }

    fun buscarCliente(documentoIdentidad: String): Cliente? {
        return clientes.find { it.documentoIdentidad == documentoIdentidad }
    }
}

// Función principal para ejecutar el programa de consola
fun main() {
    val scanner = Scanner(System.`in`)

    // Crear una empresa
    val empresa = Empresa("Mi Empresa", "123456789", "Calle Ejemplo 123")

    while (true) {
        println("\n1. Gestionar empleados")
        println("2. Gestionar clientes")
        println("3. Calcular nómina")
        println("4. Calcular nómina por dependencia")
        println("5. Mostrar porcentajes de clientes por sexo")
        println("6. Cantidad de empleados por cargo")
        println("7. Empleado con más tiempo en la empresa")
        println("8. Salir")
        print("Selecciona una opción: ")

        when (scanner.nextInt()) {
            1 -> {
                gestionarEmpleados(scanner, empresa)
            }
            2 -> {
                gestionarClientes(scanner, empresa)
            }
            3 -> {
                println("Nómina total de la empresa: ${empresa.calcularNomina()}")
            }
            4 -> {
                // Selección de dependencia
                val dependencias = listOf("ventas", "recursos humanos", "gerencia", "operativo")
                var dependencia: Dependencia?
                while (true) {
                    println("Selecciona la dependencia:")
                    dependencias.forEachIndexed { index, dep -> println("${index + 1}: $dep") }
                    val seleccion = scanner.nextInt()
                    if (seleccion in 1..dependencias.size) {
                        dependencia = Dependencia(dependencias[seleccion - 1])
                        break
                    } else {
                        println("Opción no válida. Por favor, selecciona una opción válida.")
                    }
                }
                println("Nómina para la dependencia '${dependencia.nombre}': ${empresa.calcularNominaPorDependencia(dependencia)}")
            }
            5 -> {
                empresa.mostrarPorcentajesDeClientesPorSexo()
            }
            6 -> {
                println("Introduce el nombre del cargo: ")
                val nombreCargo = scanner.next()
                println("Cantidad de empleados con cargo '$nombreCargo': ${empresa.cantidadEmpleadosPorCargo(nombreCargo)}")
            }
            7 -> {
                val empleado = empresa.empleadoConMasTiempo()
                if (empleado != null) {
                    println("Empleado con más tiempo en la empresa: Nombre: ${empleado.nombre}, Dependencia: ${empleado.dependencia.nombre}, Año de ingreso: ${empleado.añoIngreso}")
                } else {
                    println("No hay empleados en la empresa.")
                }
            }
            8 -> {
                println("Saliendo...")
                break
            }
            else -> println("Opción no válida. Inténtalo de nuevo.")
        }
    }
}

// Función para gestionar empleados
fun gestionarEmpleados(scanner: Scanner, empresa: Empresa) {
    while (true) {
        println("\n1. Añadir empleado")
        println("2. Eliminar empleado")
        println("3. Actualizar empleado")
        println("4. Buscar empleado")
        println("5. Volver al menú principal")
        print("Selecciona una opción: ")

        when (scanner.nextInt()) {
            1 -> {
                // Añadir empleado
                println("Introduce el nombre del empleado: ")
                val nombre = scanner.next()
                println("Introduce el documento de identidad del empleado: ")
                val docId = scanner.next()

                val sexo = seleccionarSexo(scanner)
                
                println("Introduce el correo electrónico del empleado: ")
                val correo = scanner.next()
                println("Introduce el salario del empleado: ")
                val salario = scanner.nextDouble()
                // Selección de dependencia
                val dependencias = listOf("ventas", "recursos humanos", "gerencia", "operativo")
                var dependencia: Dependencia?
                while (true) {
                    println("Selecciona la dependencia del empleado:")
                    dependencias.forEachIndexed { index, dep -> println("${index + 1}: $dep") }
                    val seleccion = scanner.nextInt()
                    if (seleccion in 1..dependencias.size) {
                        dependencia = Dependencia(dependencias[seleccion - 1])
                        break
                    } else {
                        println("Opción no válida. Por favor, selecciona una opción válida.")
                    }
                }
                println("Introduce el año de ingreso del empleado: ")
                val añoIngreso = scanner.nextInt()
                println("Introduce el nombre del cargo del empleado: ")
                val nombreCargo = scanner.next()
                println("Introduce el nivel jerárquico del cargo del empleado: ")
                val nivelJerarquico = scanner.nextInt()

                val cargo = Cargo(nombreCargo, nivelJerarquico)
                val empleado = Empleado(nombre, docId, sexo, correo, salario, dependencia, añoIngreso, cargo)
                empresa.añadirEmpleado(empleado)
                println("Empleado añadido correctamente.")
            }
            2 -> {
                // Eliminar empleado
                println("Introduce el documento de identidad del empleado a eliminar: ")
                val docId = scanner.next()
                empresa.eliminarEmpleado(docId)
                println("Empleado eliminado correctamente.")
            }
            3 -> {
                // Actualizar empleado
                println("Introduce el documento de identidad del empleado a actualizar: ")
                val docId = scanner.next()
                val empleadoExistente = empresa.buscarEmpleado(docId)
                if (empleadoExistente != null) {
                    println("Introduce el nuevo nombre del empleado: ")
                    val nombre = scanner.next()

                    val sexo = seleccionarSexo(scanner)
                    
                    println("Introduce el nuevo correo electrónico del empleado: ")
                    val correo = scanner.next()
                    println("Introduce el nuevo salario del empleado: ")
                    val salario = scanner.nextDouble()
                    // Selección de dependencia
                    val dependencias = listOf("ventas", "recursos humanos", "gerencia", "operativo")
                    var dependencia: Dependencia?
                    while (true) {
                        println("Selecciona la nueva dependencia del empleado:")
                        dependencias.forEachIndexed { index, dep -> println("${index + 1}: $dep") }
                        val seleccion = scanner.nextInt()
                        if (seleccion in 1..dependencias.size) {
                            dependencia = Dependencia(dependencias[seleccion - 1])
                            break
                        } else {
                            println("Opción no válida. Por favor, selecciona una opción válida.")
                        }
                    }
                    println("Introduce el nuevo año de ingreso del empleado: ")
                    val añoIngreso = scanner.nextInt()
                    println("Introduce el nuevo nombre del cargo del empleado: ")
                    val nombreCargo = scanner.next()
                    println("Introduce el nuevo nivel jerárquico del cargo del empleado: ")
                    val nivelJerarquico = scanner.nextInt()

                    val cargo = Cargo(nombreCargo, nivelJerarquico)
                    val nuevoEmpleado = Empleado(nombre, docId, sexo, correo, salario, dependencia, añoIngreso, cargo)
                    empresa.actualizarEmpleado(docId, nuevoEmpleado)
                    println("Empleado actualizado correctamente.")
                } else {
                    println("Empleado no encontrado.")
                }
            }
            4 -> {
                // Buscar empleado
                println("Introduce el documento de identidad del empleado a buscar: ")
                val docId = scanner.next()
                val empleado = empresa.buscarEmpleado(docId)
                if (empleado != null) {
                    println("Empleado encontrado: Nombre: ${empleado.nombre}, Sexo: ${empleado.sexo}, Correo: ${empleado.correoElectronico}, Salario: ${empleado.salario}, Dependencia: ${empleado.dependencia.nombre}, Año de ingreso: ${empleado.añoIngreso}, Cargo: ${empleado.cargo.nombre},  Nivel jerárquico: ${empleado.cargo.nivelJerarquico}")

                } else {
                    println("Empleado no encontrado.")
                }
            }
            5 -> {
                println("Volviendo al menú principal...")
                break
            }
            else -> println("Opción no válida. Inténtalo de nuevo.")
        }
    }
}

// Función para gestionar clientes
fun gestionarClientes(scanner: Scanner, empresa: Empresa) {
    while (true) {
        println("\n1. Añadir cliente")
        println("2. Eliminar cliente")
        println("3. Actualizar cliente")
        println("4. Buscar cliente")
        println("5. Volver al menú principal")
        print("Selecciona una opción: ")

        when (scanner.nextInt()) {
            1 -> {
                // Añadir cliente
                println("Introduce el nombre del cliente: ")
                val nombre = scanner.next()
                println("Introduce el documento de identidad del cliente: ")
                val docId = scanner.next()

                val sexo = seleccionarSexo(scanner)
                
                println("Introduce el correo electrónico del cliente: ")
                val correo = scanner.next()
                println("Introduce la dirección del cliente: ")
                val direccion = scanner.next()
                println("Introduce el teléfono del cliente: ")
                val telefono = scanner.next()

                val cliente = Cliente(nombre, docId, sexo, correo, direccion, telefono)
                empresa.añadirCliente(cliente)
                println("Cliente añadido correctamente.")
            }
            2 -> {
                // Eliminar cliente
                println("Introduce el documento de identidad del cliente a eliminar: ")
                val docId = scanner.next()
                empresa.eliminarCliente(docId)
                println("Cliente eliminado correctamente.")
            }
            3 -> {
                // Actualizar cliente
                println("Introduce el documento de identidad del cliente a actualizar: ")
                val docId = scanner.next()
                val clienteExistente = empresa.buscarCliente(docId)
                if (clienteExistente != null) {
                    println("Introduce el nuevo nombre del cliente: ")
                    val nombre = scanner.next()

                    val sexo = seleccionarSexo(scanner)

                    println("Introduce el nuevo correo electrónico del cliente: ")
                    val correo = scanner.next()
                    println("Introduce la nueva dirección del cliente: ")
                    val direccion = scanner.next()
                    println("Introduce el nuevo teléfono del cliente: ")
                    val telefono = scanner.next()

                    val nuevoCliente = Cliente(nombre, docId, sexo, correo, direccion, telefono)
                    empresa.actualizarCliente(docId, nuevoCliente)
                    println("Cliente actualizado correctamente.")
                } else {
                    println("Cliente no encontrado.")
                }
            }
            4 -> {
                // Buscar cliente
                println("Introduce el documento de identidad del cliente a buscar: ")
                val docId = scanner.next()
                val cliente = empresa.buscarCliente(docId)
                if (cliente != null) {
                    println("Cliente encontrado: Nombre: ${cliente.nombre}, Sexo: ${cliente.sexo}, Correo: ${cliente.correoElectronico}, Dirección: ${cliente.direccion}, Teléfono: ${cliente.telefono}")
                } else {
                    println("Cliente no encontrado.")
                }
            }
            5 -> {
                println("Volviendo al menú principal...")
                break
            }
            else -> println("Opción no válida. Inténtalo de nuevo.")
        }
    }
}

fun seleccionarSexo(scanner: Scanner): String {
    while (true) {
        println("Selecciona el sexo:")
        println("1. Masculino")
        println("2. Femenino")
        println("3. Otros")
        val seleccion = scanner.nextInt()
        return when (seleccion) {
            1 -> "Masculino"
            2 -> "Femenino"
            3 -> "Otro"
            else -> {
                println("Opción no válida. Por favor, selecciona una opción válida.")
                continue  // Vuelve a pedir la selección de sexo
            }
        }
    }
}