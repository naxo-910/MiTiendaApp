package cl.mitienda.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cl.mitienda.app.databinding.ActivityMainBinding
import android.util.Patterns
import android.widget.Toast
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- LÓGICA DE VALIDACIÓN MEJORADA (IE 2.2.1) ---
        binding.buttonLogin.setOnClickListener {

            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            // Reseteamos errores antiguos
            binding.editTextEmail.error = null
            binding.editTextPassword.error = null

            // --- NUEVA LÓGICA DE VALIDACIÓN ---

            // 1. Revisamos si el email está vacío
            if (email.isEmpty()) {
                binding.editTextEmail.error = "Este campo es obligatorio"
            }
            // 2. NUEVO: Revisamos si el email NO tiene formato válido
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextEmail.error = "Formato de correo inválido (ej: usuario@dominio.com)"
            }
            // 3. Revisamos si la contraseña está vacía
            else if (password.isEmpty()) {
                binding.editTextPassword.error = "Este campo es obligatorio"
            }
            // 4. NUEVO: Revisamos si la contraseña es muy corta
            else if (password.length < 6) {
                binding.editTextPassword.error = "La contraseña debe tener al menos 6 caracteres"
            }

            else {
                // ¡ÉXITO! Todos los campos son válidos.
                // Mostramos un mensaje temporal de éxito
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}