package cl.mitienda.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cl.mitienda.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener {

            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            binding.editTextEmail.error = null
            binding.editTextPassword.error = null

            if (email.isEmpty()) {
                binding.editTextEmail.error = "Este campo es obligatorio"
            }
            else if (password.isEmpty()) {
                binding.editTextPassword.error = "Este campo es obligatorio"
            }

            else {
                // Lógica de éxito (la añadiremos después)
            }
        }
    }
}