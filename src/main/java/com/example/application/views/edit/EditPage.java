package com.example.application.views.edit;

import com.example.application.data.*;
import com.example.application.services.*;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@PageTitle("Edit page")
@Route("edit-page")
@Menu(order = 4)
@RolesAllowed("ADMIN")
public class EditPage extends Div {

    private final StaffService staffService;
    private final CustomerService customerService;
    private final OperationService operationService;
    private final OrderService orderService;

    private final ComboBox<String> entitySelector = new ComboBox<>("Entity");
    private Grid<?> dynamicGrid = new Grid<>();
    private final VerticalLayout formLayout = new VerticalLayout();

    private final VerticalLayout leftSide = new VerticalLayout();

    private Filters filters;

    public EditPage(
            StaffService staffService,
            CustomerService customerService,
            OperationService operationService,
            OrderService orderService
    ) {
        this.staffService = staffService;
        this.customerService = customerService;
        this.operationService = operationService;
        this.orderService = orderService;
        SplitLayout splitLayout = new SplitLayout();

        setSizeFull();
        entitySelector.setItems("Staff", "Customer", "Operation", "Order");
        entitySelector.setPlaceholder("Choose entity");

        leftSide.add(entitySelector, dynamicGrid);
        leftSide.setSizeFull();
        dynamicGrid.setSizeFull();
        dynamicGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        entitySelector.addValueChangeListener(e -> loadGridData(e.getValue()));

        splitLayout.addToPrimary(leftSide);
        splitLayout.addToSecondary(formLayout);
        splitLayout.setSizeFull();
        add(splitLayout);
    }

    private void loadGridData(String entityName) {
        formLayout.removeAll();
        if (dynamicGrid != null) {
            leftSide.remove(dynamicGrid);
        }

        if (filters != null) {
            leftSide.remove(filters);
            filters = null;
        }

        switch (entityName) {
            case "Staff" -> {
                Grid<Staff> grid = new Grid<>(Staff.class, false);
                grid.setItems(query -> staffService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
                grid.addColumn(Staff::getFirstName).setHeader("First name").setAutoWidth(true);
                grid.addColumn(Staff::getLastName).setHeader("Last name").setAutoWidth(true);
                grid.addColumn(Staff::getEmail).setHeader("Email").setAutoWidth(true);
                grid.addColumn(Staff::getPhone).setHeader("Phone").setAutoWidth(true);
                grid.addColumn(Staff::getRole).setHeader("Role").setAutoWidth(true);

                grid.asSingleSelect().addValueChangeListener(event -> {
                    Staff selected = event.getValue();
                    if (selected != null) showPersonForm(selected);
                });

                dynamicGrid = grid;
                showPersonForm(new Staff());
            }

            case "Customer" -> {
                Grid<Customer> grid = new Grid<>(Customer.class, false);
                grid.setItems(customerService.getAll());
                grid.addColumn(Customer::getName).setHeader("Name").setAutoWidth(true);
                grid.addColumn(Customer::getEmail).setHeader("Email").setAutoWidth(true);
                grid.addColumn(Customer::getPhone).setHeader("Phone").setAutoWidth(true);

                grid.asSingleSelect().addValueChangeListener(event -> {
                    Customer selected = event.getValue();
                    if (selected != null) showCustomerForm(selected);
                });
                dynamicGrid = grid;
                showCustomerForm(new Customer());
            }

            case "Operation" -> {
                Grid<Operation> grid = new Grid<>(Operation.class, false);
                grid.setItems(operationService.getAll());
                grid.addColumn(Operation::getName).setHeader("Name").setAutoWidth(true);
                grid.addColumn(Operation::getPrice).setHeader("Price").setAutoWidth(true);

                grid.asSingleSelect().addValueChangeListener(event -> {
                    Operation selected = event.getValue();
                    if (selected != null) showServiceForm(selected);
                });
                dynamicGrid = grid;
                showServiceForm(new Operation());
            }

            case "Order" -> {
                Grid<Order> grid = new Grid<>(Order.class, false);

                filters = new Filters(this::refreshGrid, this.customerService, this.staffService);
                leftSide.add(filters);


                grid.setItems(query -> orderService.getFilteredOrders(filters, query.getOffset(), query.getLimit()).stream());
                grid.addColumn(Order::getId).setHeader("ID").setAutoWidth(true);
                grid.addColumn(o -> o.getCustomer().getName()).setHeader("Customer name").setAutoWidth(true);
                grid.addColumn(o -> o.getStaff().getFullName()).setHeader("Staff").setAutoWidth(true);
                grid.addColumn(o -> o.getStartDate().toString()).setHeader("Start date").setAutoWidth(true);
                grid.addColumn(o -> o.getOperations().stream()
                        .map(Operation::getName)
                        .collect(Collectors.joining(", "))).setHeader("Operation").setAutoWidth(true);

                grid.asSingleSelect().addValueChangeListener(event -> {
                    Order selected = event.getValue();
                    if (selected != null) showOrderForm(selected);
                });
                dynamicGrid = grid;
                showOrderForm(new Order());
            }
        }

        dynamicGrid.setSizeFull();
        dynamicGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        leftSide.add(dynamicGrid);
    }
    private void refreshGrid() {
        dynamicGrid.select(null);
        dynamicGrid.getDataProvider().refreshAll();
    }


    private void showPersonForm(Staff staff) {
        formLayout.removeAll();
        TextField firstName = new TextField("First name");
        TextField lastName = new TextField("Last name");
        TextField email = new TextField("Email");
        TextField phone = new TextField("Phone");
        MultiSelectComboBox<Role> roleComboBox = new MultiSelectComboBox<>("Roles");
        roleComboBox.setItems(Role.values());
        roleComboBox.setItemLabelGenerator(Role::name);

        firstName.setValue(staff.getFirstName() != null ? staff.getFirstName() : "");
        lastName.setValue(staff.getLastName() != null ? staff.getLastName() : "");
        email.setValue(staff.getEmail() != null ? staff.getEmail() : "");
        phone.setValue(staff.getPhone() != null ? staff.getPhone() : "");
        roleComboBox.setValue(staff.getRole());

        Button save = new Button("Save", e -> {
            try {
                staff.setFirstName(firstName.getValue());
                staff.setLastName(lastName.getValue());
                staff.setPhone(phone.getValue());
                staff.setEmail(email.getValue());
                staff.setRole(roleComboBox.getValue());

                staffService.save(staff);
                loadGridData("Staff");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button clear = new Button("Clear", e -> {
            firstName.clear();
            lastName.clear();
            email.clear();
            phone.clear();
            roleComboBox.setValue(Collections.emptySet());
        });

        Button delete = new Button("Delete", e -> {
            staffService.delete(staff.getId());
            loadGridData("Staff");
        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        formLayout.add(new FormLayout(firstName, lastName, email, phone, roleComboBox), new HorizontalLayout(save, clear, delete));
    }

    private void showCustomerForm(Customer customer) {
        formLayout.removeAll();
        TextField name = new TextField("Name");
        TextField email = new TextField("Email");
        TextField phone = new TextField("Phone");

        name.setValue(customer.getName() != null ? customer.getName() : "");
        email.setValue(customer.getEmail() != null ? customer.getEmail() : "");
        phone.setValue(customer.getPhone() != null ? customer.getPhone() : "");

        Button save = new Button("Save", e -> {
            customer.setName(name.getValue());
            customer.setEmail(email.getValue());
            customer.setPhone(phone.getValue());
            customerService.save(customer);
            loadGridData("Customer");
        });

        Button clear = new Button("Clear", e -> {
            name.clear();
            email.clear();
            phone.clear();
        });

        Button delete = new Button("Delete", e -> {
            staffService.delete(customer.getId());
            loadGridData("Customer");
        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        formLayout.add(new FormLayout(name, email, phone), new HorizontalLayout(save, clear, delete));
    }

    private void showServiceForm(Operation operation) {
        formLayout.removeAll();
        TextField name = new TextField("Name");
        TextField price = new TextField("Price");

        name.setValue(operation.getName() == null ? "" : operation.getName());
        price.setValue(operation.getPrice() == null ? "" : operation.getPrice().toString());

        Button save = new Button("Save", e -> {
            operation.setName(name.getValue());
            operation.setPrice(Double.valueOf(price.getValue()));
            operationService.save(operation);
            loadGridData("Operation");
        });

        Button clear = new Button("Clear", e -> {
            name.clear();
            price.clear();
        });

        Button delete = new Button("Delete", e -> {
            operationService.delete(operation.getId());
            loadGridData("Operation");
        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        formLayout.add(new FormLayout(name, price), new HorizontalLayout(save, clear, delete));
    }

    private void showOrderForm(Order order) {
        formLayout.removeAll();

        ComboBox<Customer> customerComboBox = new ComboBox<>("Customer");
        customerComboBox.setItems(customerService.getAll());
        customerComboBox.setItemLabelGenerator(Customer::getName);
        if (order.getCustomer() != null) {
            customerComboBox.setValue(order.getCustomer());
        }


        ComboBox<Staff> staffComboBox = new ComboBox<>("Staff");
        staffComboBox.setItems(staffService.getAll());
        staffComboBox.setItemLabelGenerator(Staff::getFullName);
        if (order.getStaff() != null) {
            staffComboBox.setValue(order.getStaff());
        }


        DateTimePicker startdateTimePicker = new DateTimePicker();
        startdateTimePicker.setLabel("Start");
        startdateTimePicker.setStep(Duration.ofMinutes(30));
        startdateTimePicker.setValue(order.getStartDate() != null ? order.getStartDate() : null);


        DateTimePicker enddateTimePicker = new DateTimePicker();
        enddateTimePicker.setLabel("End");
        enddateTimePicker.setReadOnly(true);
        if (order.getEndDate() != null) {
            enddateTimePicker.setValue(order.getEndDate());
        }


        MultiSelectComboBox<Operation> operationMultiSelect = new MultiSelectComboBox<>("Operations");
        operationMultiSelect.setItems(operationService.getAll());
        operationMultiSelect.setItemLabelGenerator(Operation::getName);
        if (order.getOperations() != null) {
            operationMultiSelect.select(order.getOperations().toArray(new Operation[0]));
        }

        operationMultiSelect.addValueChangeListener(e ->{
            if (startdateTimePicker.getValue() != null) {
                enddateTimePicker.setValue(startdateTimePicker.getValue().plusHours(1));
            }
        });
        startdateTimePicker.addValueChangeListener(e ->{
            if (startdateTimePicker.getValue() != null) {
                enddateTimePicker.setValue(startdateTimePicker.getValue().plusHours(1));
            }
        });

        Button save = new Button("Save", e -> {
            order.setCustomer(customerComboBox.getValue());
            order.setStaff(staffComboBox.getValue());
            order.setStartDate(startdateTimePicker.getValue());
            order.setEndDate(enddateTimePicker.getValue());
            order.setOperations(new ArrayList<>(operationMultiSelect.getSelectedItems()));

            orderService.save(order);
            loadGridData("Order");
        });

        Button clear = new Button("Clear", e -> {
            customerComboBox.clear();
            staffComboBox.clear();
            startdateTimePicker.clear();
            enddateTimePicker.clear();
            operationMultiSelect.clear();

        });

        Button delete = new Button("Delete", e -> {
            orderService.delete(order.getId());
            loadGridData("Order");
        });



        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        formLayout.add(new FormLayout(customerComboBox, staffComboBox, startdateTimePicker, enddateTimePicker, operationMultiSelect), new HorizontalLayout(save, clear, delete));
    }

}
