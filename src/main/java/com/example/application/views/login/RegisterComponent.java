package com.example.application.views.login;

import com.example.application.data.Customer;
import com.example.application.data.Role;
import com.example.application.services.CustomerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RegisterComponent extends Div {

    private Dialog dialog;


    public RegisterComponent(CustomerService customerService, PasswordEncoder passwordEncoder){
        dialog = new Dialog();
        Customer customer = new Customer();


        dialog.setHeaderTitle("New customer");

        FormLayout formLayout = new FormLayout();
        TextField name = new TextField("Name");

        EmailField email = new EmailField();
        email.setLabel("Email");
        email.setPlaceholder("example@aaa.com");
        email.setErrorMessage("Enter a valid email address");
        email.setClearButtonVisible(true);
        email.setPrefixComponent(VaadinIcon.ENVELOPE.create());

        HorizontalLayout phoneInput = new HorizontalLayout();

        ComboBox<String> countryCode = new ComboBox<>("Maa");
        countryCode.setItems("+358", "+46", "+7", "+1", "+48");
        countryCode.setWidth("120px");
        countryCode.setValue("+358");

        TextField phoneNumber = new TextField("Puhelin");
        phoneNumber.setPlaceholder("123456789");
        phoneNumber.setWidth("200px");

        phoneInput.add(countryCode, phoneNumber);
        phoneInput.setAlignItems(FlexComponent.Alignment.END);

        //TextField phone = new TextField(("Phone"));

        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm password");
        formLayout.add( name, email, phoneInput, password, confirmPassword);
        dialog.add(formLayout);
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);

        add(dialog);

        BeanValidationBinder<Customer> binder = new BeanValidationBinder<>(Customer.class);

        binder.forField(name).asRequired("Pakollinen kenttä")
                .withValidator(customerService::customerNameAvailable,
                        "Käyttäjänimi on varattu")
                .bind(Customer::getName, Customer::setName);
        binder.forField(email).asRequired("Pakollinen kenttä")
                .bind(Customer::getEmail, Customer::setEmail);
        binder.forField(phoneNumber).asRequired("Pakollinen kenttä")
                .withConverter(
                        number -> countryCode.getValue() + number,
                        fullPhone -> {
                            if (fullPhone != null && !fullPhone.isEmpty()){
                                for (String code : countryCode.getListDataView().getItems().toList()){
                                    if (fullPhone.startsWith(code)){
                                        countryCode.setValue(code);
                                        return fullPhone.substring(code.length());
                                    }
                                }
                            }
                            return fullPhone;
                        }
                )
                .bind(Customer::getPhone, Customer::setPhone);
        binder.forField(password).asRequired("Pakollinen kenttä")
                .withValidator(pw -> pw.length() >= 8,
                        "Salasanan pitää olla vähintään 8 merkkiä")
                .bind(Customer::getHashedPassword,
                        (user1, pw) -> user1.setHashedPassword(passwordEncoder.encode(pw)));
        binder.forField(confirmPassword).asRequired("Pakollinen kenttä")
                .withValidator(confirmed -> Objects.equals(confirmed, password.getValue()),
                        "Salasanojen täytyy olla samat")
                .bind(Customer::getHashedPassword,
                        (user1, pw) -> user1.setHashedPassword(passwordEncoder.encode(pw)));

        saveButton.addClickListener(e -> {
            binder.validate();
            if (binder.isValid()){
                try {
                    binder.writeBean(customer);

                    Set<Role> roles = new HashSet<>();
                    roles.add(Role.USER);
                    customer.setRole(roles);
                    customerService.save(customer);
                    UI.getCurrent().navigate("login");
                } catch (ValidationException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        cancelButton.addClickListener( e -> {
            name.clear();
            email.clear();
            phoneNumber.clear();
            password.clear();
            confirmPassword.clear();
            dialog.close();
        });
    }

    public void openRegisterComponent(){
        dialog.open();
    }
}
