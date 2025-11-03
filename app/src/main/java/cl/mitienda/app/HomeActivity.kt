package cl.mitienda.app

import android.Manifest // Para acceder al nombre del permiso de CÁMARA
import android.content.Intent // Para "lanzar" la cámara
import android.content.pm.PackageManager // Para "revisar" si tenemos permiso
import android.os.Bundle
import android.provider.MediaStore // Para la acción de "abrir la cámara"
import android.widget.Toast // Para mostrar mensajes
import androidx.activity.result.contract.ActivityResultContracts // El manejador de permisos moderno
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat // Para "revisar" el permiso
import cl.mitienda.app.databinding.ActivityHomeBinding // El ViewBinding para activity_home.xml

class HomeActivity : AppCompatActivity() {

    // 1. Preparamos la conexión con el "plano" (activity_home.xml)
    private lateinit var binding: ActivityHomeBinding

    // 2. Preparamos el "Lanzador de Permisos" (La forma moderna)
    // Esto registra un "contrato": Pido un permiso (String) y recibo una respuesta (Boolean: true si me lo dio, false si no)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            // Esto se ejecuta DESPUÉS de que el usuario presiona "Permitir" o "Rechazar"
            if (isGranted) {
                // El usuario dio permiso, ¡abrimos la cámara!
                openCamera()
            } else {
                // El usuario rechazó el permiso. Mostramos un mensaje.
                Toast.makeText(this, "Permiso de cámara rechazado", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 3. Conectamos el ViewBinding (igual que en MainActivity)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 4. Le decimos al botón "Abrir Cámara" que "escuche" los clics
        binding.buttonCamera.setOnClickListener {
            // Cuando el usuario hace clic, llamamos a nuestra función de revisión de permisos
            requestCameraPermission()
        }
    }

    // 5. Nuestra función para revisar el permiso
    private fun requestCameraPermission() {
        when {
            // Caso A: El usuario YA nos dio el permiso antes.
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // El permiso ya está concedido, abrimos la cámara directamente.
                openCamera()
            }

            // (Aquí podría ir un Caso B para explicar "por qué" pedimos el permiso, pero lo saltaremos por simplicidad)

            // Caso C: No tenemos el permiso. Debemos PEDIRLO.
            else -> {
                // Lanzamos el diálogo oficial de Android (el que preparamos en el paso 2)
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // 6. Nuestra función final para ABRIR la cámara
    private fun openCamera() {
        // Creamos una "Intención" de usar el servicio de "Tomar una Foto" del sistema
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Lanzamos la app de cámara del teléfono
        startActivity(intent)
    }
}