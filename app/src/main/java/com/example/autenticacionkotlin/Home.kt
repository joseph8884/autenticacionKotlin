package com.example.autenticacionkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

enum class ProviderType (){
    BASIC
}
/*Datos de autenticacion*/
lateinit var emailtextView:TextView
lateinit var providertextView:TextView
lateinit var logOutboton:Button


/*text edit*/
lateinit var telText:EditText
lateinit var dirText: EditText
lateinit var proText: EditText
lateinit var generoText: EditText

/*botones*/
lateinit var guardarBoton:Button
lateinit var eliminarBoton:Button
lateinit var mostrarBoton:Button
lateinit var plataformaBoton:Button
private val db=FirebaseFirestore.getInstance()
class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        emailtextView=findViewById(R.id.emailtextView)
        providertextView=findViewById(R.id.providertextView)
        logOutboton=findViewById(R.id.logOutboton)
        /*Edit Text*/
        telText=findViewById(R.id.telText)
        dirText=findViewById(R.id.direccionText)
        proText=findViewById(R.id.programaText)
        generoText=findViewById(R.id.generoTexto)
        /** Botones*/
        guardarBoton=findViewById(R.id.guardatBot)
        eliminarBoton=findViewById(R.id.eliminarBot)
        mostrarBoton=findViewById(R.id.mostrarBot)
        plataformaBoton=findViewById(R.id.plataformaBot)

        //setup
        val bundle=intent.extras
        val email=bundle?.getString("email")
        val provider=bundle?.getString("provider")

        setup(email ?:"", provider ?:"")
    }
    private fun blank(){
        telText.text.clear()
        dirText.text.clear()
        proText.text.clear()
        generoText.text.clear()
    }
    private fun showMaps() {
        val mapaIntent = Intent(this, Mapa::class.java)
        startActivity(mapaIntent)
    }

    private fun setup(email: String, provider: String){
        title="inicio"
        emailtextView.text=email
        providertextView.text=provider
        logOutboton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
        guardarBoton.setOnClickListener {
            db.collection("contactos").document(email).set(
                hashMapOf("telefono" to telText.text ?.toString(),
                    "direccion" to dirText.text ?.toString(),
                    "programa" to proText.text ?.toString(),
                    "genero" to generoText.text ?.toString()
                    )
            )
            blank()
        }
        eliminarBoton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminar información")
            builder.setMessage("¿Estás seguro de que deseas eliminar esta información? Esta acción no se puede deshacer.")

            // Configuración del botón de confirmación
            builder.setPositiveButton("Eliminar") { dialog, which ->
                // Aquí colocas el código para eliminar la información de Firebase
                db.collection("contactos").document(email).delete()
                Toast.makeText(this, "La información ha sido eliminada", Toast.LENGTH_SHORT).show()
            }

            // Configuración del botón de cancelar
            builder.setNegativeButton("Cancelar") { dialog, which ->
                // No hacer nada o puedes mostrar un mensaje de cancelación
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show()
            }

            // Mostrar la alerta
            val dialog = builder.create()
            dialog.show()
            blank()
        }
        mostrarBoton.setOnClickListener {
            db.collection("contactos").document(email).get().addOnSuccessListener {
                telText.setText(it.get("telefono")as String?)
                dirText.setText(it.get("direccion")as String?)
                proText.setText(it.get("programa")as String?)
                generoText.setText(it.get("genero")as String?)
            }
        }
        plataformaBoton.setOnClickListener {
            showMaps()
        }

    }
}