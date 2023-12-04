package com.example.proyectusfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class SignupTabFragment extends Fragment {

    public interface SignupListener {
        void onSignupSuccess();
    }

    public interface ViewPagerProvider {
        ViewPager2 getViewPager();
    }

    private SignupListener signupListener;
    private ViewPager2 viewPager2;
    private DatabaseHelper databaseHelper;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            signupListener = (SignupListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " debe implementar SignupListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_tab, container, false);

        // Inicializa el ayudante de base de datos
        databaseHelper = new DatabaseHelper(getActivity());

        // Encuentra las vistas
        EditText emailEditText = view.findViewById(R.id.signup_email);
        EditText passwordEditText = view.findViewById(R.id.signup_password);
        EditText confirmEditText = view.findViewById(R.id.signup_confirm);
        Button signupButton = view.findViewById(R.id.signup_button);

        // Maneja el clic del botón de registro
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene la entrada del usuario
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();

                // Valida la entrada
                if (email.isEmpty() || password.isEmpty() || !password.equals(confirm)) {
                    // Muestra un mensaje de error
                    Toast.makeText(getActivity(), "Entrada inválida o las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else {
                    // Inserta al usuario en la base de datos
                    insertarUsuario(email, password);

                    // Notifica a MainActivity sobre el registro exitoso
                    signupListener.onSignupSuccess();
                }
            }
        });

        return view;
    }

    private void insertarUsuario(String email, String password) {
        // Obtiene una referencia de base de datos de escritura
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        // Crea un objeto ContentValues para insertar datos
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        // Inserta datos en la tabla de usuarios
        long newRowId = database.insert(DatabaseHelper.TABLE_USERS, null, values);

        // Verifica si la inserción fue exitosa
        if (newRowId != -1) {
            // Muestra un mensaje de éxito o maneja según sea necesario
            Toast.makeText(getActivity(), "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            // Muestra un mensaje de error o maneja según sea necesario
            Toast.makeText(getActivity(), "Error al registrar al usuario", Toast.LENGTH_SHORT).show();
        }

        // Cierra la conexión de la base de datos
        database.close();
    }
}

