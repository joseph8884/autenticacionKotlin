package com.example.autenticacionkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.example.autenticacionkotlin.ProviderType

lateinit var regboton:Button
lateinit var acepboton:Button
lateinit var emailText:EditText
lateinit var passText:EditText
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        regboton=findViewById(R.id.regboton)
        acepboton=findViewById(R.id.acepboton)
        emailText=findViewById(R.id.emailText)
        passText=findViewById(R.id.passText)
        setup()
    }

    private fun setup(){
        regboton.setOnClickListener{
            if (emailText.text.isNotEmpty() && passText.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailText.text.toString(),
                    passText.text.toString()).addOnCompleteListener{
                        if (it.isSuccessful){
                            showHome(it.result?.user?.email ?:"",ProviderType.BASIC)
                        } else{
                            alerta()
                        }
                }
            }
        }
        acepboton.setOnClickListener {
            if (emailText.text.isNotEmpty() && passText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText.text.toString(),
                    passText.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showHome(it.result?.user?.email ?:"",ProviderType.BASIC)
                    } else{
                        alerta()
                    }
                }
            }
        }
    }
    private fun showHome(email:String,provider:ProviderType){
        val homeIntent=Intent(this, Home::class.java).apply{
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
    private fun alerta(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error al ingresar usuario")
        builder.setPositiveButton("acptar",null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }

}