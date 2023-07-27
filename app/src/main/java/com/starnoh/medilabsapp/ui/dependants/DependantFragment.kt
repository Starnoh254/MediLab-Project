package com.starnoh.medilabsapp.ui.dependants

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.starnoh.medilabsapp.R
import com.starnoh.medilabsapp.constants.Constants
import com.starnoh.medilabsapp.helpers.ApiHelper
import com.starnoh.medilabsapp.helpers.PrefsHelper
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class DependantFragment : Fragment() {
    lateinit var selectedDate:String
    private lateinit var datePickerButton: Button
    private lateinit var surname: TextInputEditText
    private lateinit var others: TextInputEditText
    var radioGroup: RadioGroup? = null
    lateinit var radioButton: RadioButton



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root = inflater.inflate(R.layout.fragment_dependant, container, false)
        datePickerButton = root.findViewById<MaterialButton>(R.id.datePickerButton)
        datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        val add_dependant = root.findViewById<MaterialButton>(R.id.add_dependant)

        surname = root.findViewById(R.id.surname)
        others = root.findViewById(R.id.others)
        radioGroup = root.findViewById(R.id.radioGroupGender)
        add_dependant.setOnClickListener {
            if(surname.text!!.isEmpty())
                surname.setError("This field is empty")
            else if (others.text!!.isEmpty()){
                others.setError("This field is required")
            }else{
                post()
            }

        }
        return root
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                // Handle the selected date here
                selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                Toast.makeText(requireContext(), "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
            },
            year,
            month,
            day
        )

        datePickerDialog.show()


    }

    private fun post(){
        val helper = ApiHelper(requireContext())
        val api = "${Constants.BASE_URL}/add_dependant"
        val member_id = PrefsHelper.getPrefs(requireContext(), "member_id")
        val body = JSONObject()
        body.put("surname",surname.text)
        body.put("others",others.text)
        body.put("gender",radioButton.text)
        body.put("dob",selectedDate)
        body.put("member_id",member_id)
        helper.post(api,body, object : ApiHelper.CallBack {
            override fun onSuccess(result: JSONArray?) {

            }

            override fun onSuccess(result: JSONObject?) {
                Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(result: String?) {

            }

        })
    }

}