package com.example.proyectusfinal;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements SignupTabFragment.SignupListener, SignupTabFragment.ViewPagerProvider {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter adapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa la base de datos
        databaseHelper = new DatabaseHelper(this);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        // Maneja el registro de usuario al hacer clic en el botón en la pestaña de registro
        handleSignupButtonClick();
    }

    private void handleSignupButtonClick() {
        // Encuentra las vistas en la pestaña de registro
        View signupTabView = viewPager2.getChildAt(1);
        if (signupTabView != null) {
            EditText emailEditText = signupTabView.findViewById(R.id.signup_email);
            EditText passwordEditText = signupTabView.findViewById(R.id.signup_password);
            EditText confirmEditText = signupTabView.findViewById(R.id.signup_confirm);
            Button signupButton = signupTabView.findViewById(R.id.signup_button);

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
                        Toast.makeText(MainActivity.this, "Entrada inválida o las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    } else {
                        // Inserta al usuario en la base de datos
                        insertarUsuario(email, password);
                    }
                }
            });
        }
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
            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();

            // Limpia los campos después de un registro exitoso
            limpiarCamposRegistro();
        } else {
            // Muestra un mensaje de error o maneja según sea necesario
            Toast.makeText(this, "Error al registrar al usuario", Toast.LENGTH_SHORT).show();
        }

        // Cierra la conexión de la base de datos
        database.close();
    }

    private void limpiarCamposRegistro() {
        // Encuentra las vistas y límpialas
        View signupTabView = viewPager2.getChildAt(1);
        if (signupTabView != null) {
            EditText emailEditText = signupTabView.findViewById(R.id.signup_email);
            EditText passwordEditText = signupTabView.findViewById(R.id.signup_password);
            EditText confirmEditText = signupTabView.findViewById(R.id.signup_confirm);

            // Limpia los campos
            emailEditText.getText().clear();
            passwordEditText.getText().clear();
            confirmEditText.getText().clear();
        }
    }

    // Método de la interfaz SignupListener
    @Override
    public void onSignupSuccess() {
        // Puedes implementar más lógica si es necesario después de un registro exitoso
        limpiarCamposRegistro();
    }

    // Método de la interfaz ViewPagerProvider
    @Override
    public ViewPager2 getViewPager() {
        return viewPager2;
    }
}
