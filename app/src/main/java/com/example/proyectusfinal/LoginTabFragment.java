// LoginTabFragment.java
package com.example.proyectusfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LoginTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_tab, container, false);

        // Encuentra las vistas
        EditText emailEditText = view.findViewById(R.id.login_email);
        EditText passwordEditText = view.findViewById(R.id.login_password);
        Button loginButton = view.findViewById(R.id.login_button);

        // Maneja el clic del botón de inicio de sesión
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene la entrada del usuario
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();

                // Validación básica (puedes agregar más validaciones según tus necesidades)
                if (isValidLogin(email, password)) {
                    // Inicio de sesión exitoso, abre la actividad HomeActivity
                    openHomeActivity();
                } else {
                    // Muestra un mensaje de error
                    Toast.makeText(getActivity(), "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean isValidLogin(String email, String password) {
        // Realiza la validación del inicio de sesión (puedes consultar la base de datos)
        // Por ahora, simplemente verifica si el email y la contraseña no están vacíos
        return !email.isEmpty() && !password.isEmpty();
    }

    private void openHomeActivity() {
        // Abre la actividad HomeActivity
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);

        // Puedes finalizar la actividad actual si no quieres que el usuario regrese al inicio de sesión
        getActivity().finish();
    }
}
