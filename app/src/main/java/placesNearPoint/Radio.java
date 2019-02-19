package placesNearPoint;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.widget.EditText;

public class Radio extends FragmentActivity {

    public interface OnOkRadio{
        void radio (int radio);
    }


    public void ingresaRadio(Context context , final OnOkRadio onOkRadio){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        final EditText edittextRadio = new EditText(context);

        edittextRadio.setInputType(InputType.TYPE_CLASS_NUMBER);
        edittextRadio.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(4) });//mmaximo 4 numeros
        edittextRadio.setKeyListener(DigitsKeyListener.getInstance(false,false));//false false desabilita numero decimales y negativos

        alert.setMessage("Ingrese el perimetro de busqueda en METROS");
        alert.setTitle("Busquedas Cercanas");

        alert.setView(edittextRadio);

        alert.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Editable YouEditTextValue = edittextRadio.getText();
                Integer rad = Integer.valueOf(String.valueOf(YouEditTextValue)) ;

                onOkRadio.radio(rad);
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }

}
