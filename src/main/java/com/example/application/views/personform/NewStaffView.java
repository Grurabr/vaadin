package com.example.application.views.personform;

import com.example.application.data.Staff;
import com.example.application.data.StaffRepository;
import com.example.application.services.StaffService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Objects;

@PageTitle("New staff")
@Route("new-staff")
@Menu(order = 1, icon = LineAwesomeIconUrl.USER)
@RolesAllowed("ADMIN")
@CssImport("./styles/global-styles.css")
public class NewStaffView extends Composite<VerticalLayout> {


    private final StaffService staffService;
    private final StaffRepository staffRepository;

    public NewStaffView(StaffService staffService,
                        StaffRepository staffRepository,
                        PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.staffService = staffService;

        Staff staff = new Staff();

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        EmailField emailField = new EmailField("Email");
        ComboBox<String> countryCode = new ComboBox<>("Maa");
        countryCode.setItems("+358", "+46", "+7", "+1", "+48");
        countryCode.setWidth("120px");
        countryCode.setValue("+358");

        HorizontalLayout phoneInput = new HorizontalLayout();
        TextField phoneNumber = new TextField("Puhelin");
        phoneNumber.setWidth("200px");

        phoneInput.add(countryCode, phoneNumber);
        phoneInput.setAlignItems(FlexComponent.Alignment.END);

        HorizontalLayout layoutRow = new HorizontalLayout();
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Personal Information");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");


        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm password");

        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        BeanValidationBinder<Staff> binder = new BeanValidationBinder<>(Staff.class);

        binder.forField(firstName).asRequired("Pakollinen kenttä")
                .bind(Staff::getFirstName, Staff::setFirstName);
        binder.forField(lastName).asRequired("Pakollinen kenttä")
                .bind(Staff::getLastName, Staff::setLastName);

        binder.forField(emailField).asRequired("Pakollinen kenttä")
                .withValidator(staffService::customerEmailAvailable,
                        "Käyttäjän email on varattu")
                .bind(Staff::getEmail, Staff::setEmail);
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
                .bind(Staff::getPhone, Staff::setPhone);
        binder.forField(password).asRequired("Pakollinen kenttä")
                .withValidator(pw -> pw.length() >= 8,
                        "Salasanan pitää olla vähintään 8 merkkiä")
                .bind(Staff::getHashedPassword,
                        (user1, pw) -> user1.setHashedPassword(passwordEncoder.encode(pw)));
        binder.forField(confirmPassword).asRequired("Pakollinen kenttä")
                .withValidator(confirmed -> Objects.equals(confirmed, password.getValue()),
                        "Salasanojen täytyy olla samat")
                .bind(Staff::getHashedPassword,
                        (user1, pw) -> user1.setHashedPassword(passwordEncoder.encode(pw)));

        saveButton.addClickListener(e -> {
            binder.validate();
            if (binder.isValid()){
                try {
                    binder.writeBean(staff);
                    staffService.save(staff);
                    UI.getCurrent().navigate("login");
                } catch (ValidationException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        cancelButton.addClickListener( e -> {
            firstName.clear();
            lastName.clear();
            emailField.clear();
            phoneNumber.clear();
            password.clear();
            confirmPassword.clear();
        });


        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(firstName);
        formLayout2Col.add(lastName);
        formLayout2Col.add(phoneInput);
        formLayout2Col.add(emailField);
        formLayout2Col.add(password);
        formLayout2Col.add(confirmPassword);
        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(cancelButton);
    }
}
