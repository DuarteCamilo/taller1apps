import java.util.Scanner

open class Persona(
    var nombre: String,
    var documentoIdentidad: String,
    var sexo: String,
    var correoElectronico: String
)

class Empleado(
    nombre: String,
    documentoIdentidad: String,
    sexo: String,
    correoElectronico: String,
    var salario: Double,
    var dependencia: Dependencia,
    var anioIngreso: Int,
    var cargo: Cargo,
    var subordinados: MutableList<Empleado> = mutableListOf() 
) : Persona(nombre, documentoIdentidad, sexo, correoElectronico)

class Cliente(
    nombre: String,
    documentoIdentidad: String,
    sexo: String,
    correoElectronico: String,
    var direccion: String,
    var telefono: String
) : Persona(nombre, documentoIdentidad, sexo, correoElectronico)

class Cargo(
    var nombre: String,
    var nivelJerarquico: Int
)

class Dependencia(
    var nombre: String
)

class Empresa(
    var razonSocial: String,
    var nit: String,
    var direccion: String,
    var empleados: MutableList<Empleado> = mutableListOf(),
    var clientes: MutableList<Cliente> = mutableListOf()
) {
    fun calcularNomina(): Double {
        return empleados.sumOf { it.salario }
    }

    fun calcularNominaPorDependencia(dependencia: Dependencia): Double {
        return empleados.filter { it.dependencia.nombre == dependencia.nombre }.sumOf { it.salario }
    }

    fun porcentajeClientesPorSexo(sexo: String): Double {
        val totalClientes = clientes.size
        val clientesPorSexo = clientes.count { it.sexo == sexo }
        return if (totalClientes > 0) (clientesPorSexo.toDouble() / totalClientes) * 100 else 0.0
    }

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

    fun cantidadEmpleadosPorCargo(nombreCargo: String): Int {
        return empleados.count { it.cargo.nombre == nombreCargo }
    }

    fun empleadoConMasTiempo(): Empleado? {
        return empleados.minByOrNull { it.anioIngreso }
    }

    fun añadirEmpleado(empleado: Empleado) {
        empleados.add(empleado)
    }

    fun eliminarEmpleado(documentoIdentidad: String) {
        empleados.find { it.documentoIdentidad == documentoIdentidad }?.let { empleado ->
            empleados.filter { it.subordinados.contains(empleado) }.forEach { jefe ->
                jefe.subordinados.remove(empleado)
            }
            empleados.removeIf { it.documentoIdentidad == documentoIdentidad }
        }
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

fun main() {
    val scanner = Scanner(System.`in`)

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
                    println("Empleado con más tiempo en la empresa: Nombre: ${empleado.nombre}, Dependencia: ${empleado.dependencia.nombre}, Año de ingreso: ${empleado.anioIngreso}")
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

fun gestionarEmpleados(scanner: Scanner, empresa: Empresa) {
    while (true) {
        println("\n1. Añadir empleado")
        println("2. Eliminar empleado")
        println("3. Actualizar empleado")
        println("4. Buscar empleado")
        println("5. Gestionar subordinados")
        println("6. Ver subordinados de un empleado")
        println("7. Volver al menú principal")
        print("Selecciona una opción: ")

        when (scanner.nextInt()) {
            1 -> {
                println("Introduce el nombre del empleado: ")
                val nombre = scanner.next()
                println("Introduce el documento de identidad del empleado: ")
                val docId = scanner.next()

                val sexo = seleccionarSexo(scanner)

                println("Introduce el correo electrónico del empleado: ")
                val correo = scanner.next()
                println("Introduce el salario del empleado: ")
                val salario = scanner.nextDouble()
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
                val anioIngreso = scanner.nextInt()
                println("Introduce el nombre del cargo del empleado: ")
                val nombreCargo = scanner.next()
                println("Introduce el nivel jerárquico del cargo del empleado: ")
                val nivelJerarquico = scanner.nextInt()
                val cargo = Cargo(nombreCargo, nivelJerarquico)
                val empleado = Empleado(nombre, docId, sexo, correo, salario, dependencia, anioIngreso, cargo)
                empresa.añadirEmpleado(empleado)
                println("Empleado añadido correctamente.")
            }
            2 -> {
                println("Introduce el documento de identidad del empleado a eliminar: ")
                val docId = scanner.next()
                empresa.eliminarEmpleado(docId)
                println("Empleado eliminado correctamente.")
            }
            3 -> {
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
                    val anioIngreso = scanner.nextInt()
                    println("Introduce el nuevo nombre del cargo del empleado: ")
                    val nombreCargo = scanner.next()
                    println("Introduce el nuevo nivel jerárquico del cargo del empleado: ")
                    val nivelJerarquico = scanner.nextInt()

                    val cargo = Cargo(nombreCargo, nivelJerarquico)
                    val nuevoEmpleado = Empleado(nombre, docId, sexo, correo, salario, dependencia, anioIngreso, cargo)
                    empresa.actualizarEmpleado(docId, nuevoEmpleado)
                    println("Empleado actualizado correctamente.")
                } else {
                    println("Empleado no encontrado.")
                }
            }
            4 -> {
                println("Introduce el documento de identidad del empleado a buscar: ")
                val docId = scanner.next()
                val empleado = empresa.buscarEmpleado(docId)
                if (empleado != null) {
                    println("Empleado encontrado: Nombre: ${empleado.nombre}, Sexo: ${empleado.sexo}, Correo: ${empleado.correoElectronico}, Salario: ${empleado.salario}, Dependencia: ${empleado.dependencia.nombre}, Año de ingreso: ${empleado.anioIngreso}, Cargo: ${empleado.cargo.nombre}, Nivel jerárquico: ${empleado.cargo.nivelJerarquico}")

                } else {
                    println("Empleado no encontrado.")
                }
            }
            5 -> {
                gestionarSubordinados(scanner, empresa)
            }
            6 -> {
                println("Introduce el documento de identidad del empleado para ver sus subordinados: ")
                val docId = scanner.next()
                val empleado = empresa.buscarEmpleado(docId)
                if (empleado != null) {
                    if (empleado.subordinados.isNotEmpty()) {
                        println("Subordinados de ${empleado.nombre}:")
                        empleado.subordinados.forEach {
                            println("Nombre: ${it.nombre}, ID: ${it.documentoIdentidad}")
                        }
                    } else {
                        println("El empleado no tiene subordinados.")
                    }
                } else {
                    println("Empleado no encontrado.")
                }
            }
            7 -> {
                println("Volviendo al menú principal...")
                break
            }
            else -> println("Opción no válida. Inténtalo de nuevo.")
        }
    }
}

fun gestionarSubordinados(scanner: Scanner, empresa: Empresa) {
    println("Introduce el documento de identidad del jefe para gestionar sus subordinados: ")
    val docId = scanner.next()
    val jefe = empresa.buscarEmpleado(docId)
    if (jefe != null) {
        while (true) {
            println("\n1. Añadir subordinado")
            println("2. Eliminar subordinado")
            println("3. Volver al menú de empleados")
            print("Selecciona una opción: ")

            when (scanner.nextInt()) {
                1 -> {
                    println("Introduce el documento de identidad del subordinado a añadir: ")
                    val docSubordinado = scanner.next()
                    val subordinado = empresa.buscarEmpleado(docSubordinado)
                    if (subordinado != null && subordinado.documentoIdentidad != jefe.documentoIdentidad) {
                        if (jefe.subordinados.contains(subordinado)) {
                            println("El subordinado ya está en la lista.")
                        } else {
                            jefe.subordinados.add(subordinado)
                            println("Subordinado añadido correctamente.")
                        }
                    } else {
                        println("Subordinado no encontrado o el documento de identidad es el mismo que el del jefe.")
                    }
                }
                2 -> {
                    println("Introduce el documento de identidad del subordinado a eliminar: ")
                    val docSubordinado = scanner.next()
                    val subordinado = jefe.subordinados.find { it.documentoIdentidad == docSubordinado }
                    if (subordinado != null) {
                        jefe.subordinados.remove(subordinado)
                        println("Subordinado eliminado correctamente.")
                    } else {
                        println("Subordinado no encontrado.")
                    }
                }
                3 -> {
                    println("Volviendo al menú de empleados...")
                    break
                }
                else -> println("Opción no válida. Inténtalo de nuevo.")
            }
        }
    } else {
        println("Empleado no encontrado.")
    }
}

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
                println("Introduce el documento de identidad del cliente a eliminar: ")
                val docId = scanner.next()
                empresa.eliminarCliente(docId)
                println("Cliente eliminado correctamente.")
            }
            3 -> {
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
        println("3. Otro")
        val seleccion = scanner.nextInt()
        return when (seleccion) {
            1 -> "Masculino"
            2 -> "Femenino"
            3 -> "Otro"
            else -> {
                println("Opción no válida. Por favor, selecciona una opción válida.")
                continue
            }
        }
    }
}
