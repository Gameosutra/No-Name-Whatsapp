package com.cellfishpool.nonamewhatsapp



import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.lamudi.phonefield.PhoneEditText
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.ComponentName
import android.content.Context
import java.net.URISyntaxException
import android.view.inputmethod.InputMethodManager
import android.util.Log
import android.widget.ImageView


class MainActivity : AppCompatActivity() {


    var imageView:ImageView?=null
    var phoneno: String="123"
    var final_msg:String="Hey"
    var valid=true
    var msg:EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        setSupportActionBar(findViewById(R.id.toolbar))

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        var spref= getPreferences(Context.MODE_PRIVATE)


        if(spref.getBoolean("open",true)) {
            var editor=spref.edit()
            editor.putBoolean("open",false)
            editor.apply()
            Log.i("Hello",spref.getBoolean("open",false).toString())
            var i = Intent(this, IntroActivity::class.java)
            startActivity(i)
        }
        imageView=findViewById(R.id.imageView)
        var phone=findViewById<PhoneEditText>(R.id.phone)
        msg=findViewById(R.id.msg)
        var chat=findViewById<Button>(R.id.chat)

        imageView?.setOnClickListener {
            val intent = Intent(this@MainActivity, IntroActivity::class.java)
            startActivity(intent)
        }

        phone.setDefaultCountry("IN")
        chat.setOnClickListener{
            validate(phone)
            if(valid==false) {
                preprocess()
                openInWhatsapp()
            }
        }
//
    }



    private fun preprocess() {

        phoneno=phoneno.replace("+", "");
        phoneno=phoneno.replace("^0", "");
        final_msg = msg!!.text.toString().replace("\\s".toRegex(), "%20")
        Log.i("Hello",final_msg)

    }


    private fun validate(phone: PhoneEditText) {

            if (phone.isValid) {
                phone.setError(null)
                hideKeyboard(phone)
                valid=false
            } else{
                phone.setError("Invalid Phone Number")
                hideKeyboard(phone)
            }


        phoneno = phone.getPhoneNumber()
    }



    private fun openInWhatsapp() {
        try {

            Toast.makeText(applicationContext,"Processing...",Toast.LENGTH_SHORT).show()
            val intent = Intent("android.intent.action.MAIN")
            intent.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
            intent.putExtra("jid", phoneno + "@s.whatsapp.net")
            intent.putExtra("displayname", "+" + phoneno)
            startActivity(Intent.parseUri("https://api.whatsapp.com/send?phone=" + phoneno+"&text="+final_msg, 0))
            //startActivity(Intent.parseUri("whatsapp://send/?" + phoneno, 0))
        } catch (ignore: URISyntaxException) {
            ignore.printStackTrace()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext, "Whatsapp not installed", Toast.LENGTH_LONG).show()
        }

    }

    @SuppressLint("ServiceCast")
    protected fun hideKeyboard(v: View) {
        val imm = v.context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

}
