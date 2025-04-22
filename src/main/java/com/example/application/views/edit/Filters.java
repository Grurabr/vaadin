package com.example.application.views.edit;

import com.example.application.data.Customer;
import com.example.application.data.Order;
import com.example.application.data.Staff;
import com.example.application.services.CustomerService;
import com.example.application.services.OperationService;
import com.example.application.services.StaffService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.data.jpa.domain.Specification;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;

import jakarta.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class Filters extends HorizontalLayout implements Specification<Order> {
    private TextField idOrder = new TextField("ID");
    private ComboBox<Customer> customerComboBox = new ComboBox<Customer>("Customer");
    private MultiSelectComboBox<Staff> staffMultiSelectComboBox = new MultiSelectComboBox<>("Staff");
    private DatePicker startDate = new DatePicker("Start date");
    private DatePicker endDate = new DatePicker("End date");

    private Button searchButton = new Button("Search");
    private Button clearButton = new Button("Clear");

    public Filters(Runnable onSearch, CustomerService customerService, StaffService staffService){
        customerComboBox.setItems(customerService.getAll());
        customerComboBox.setItemLabelGenerator(Customer::getName);

        staffMultiSelectComboBox.setItems(staffService.getAll());
        staffMultiSelectComboBox.setItemLabelGenerator(Staff::getFullName);

        clearButton.addClickListener(event -> {
            customerComboBox.clear();
            idOrder.clear();
            staffMultiSelectComboBox.clear();
            startDate.clear();
            endDate.clear();
            onSearch.run();
        });

        searchButton.addClickListener(event -> {
            onSearch.run();
        });

        HorizontalLayout butLayout = new HorizontalLayout();
        butLayout.add(searchButton, clearButton);

        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.getStyle().set("flex-wrap", "wrap");
        layout.setAlignItems(Alignment.END);
        layout.add(idOrder, customerComboBox, staffMultiSelectComboBox, startDate,
                endDate, butLayout);
        add(layout);

    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();


        if (!idOrder.isEmpty()) {
            Predicate idPredicate = criteriaBuilder.equal(root.get("id"), Long.valueOf(idOrder.getValue()));
            predicates.add(idPredicate);
        }

        if (customerComboBox.getValue() != null) {
            Join<Order, Customer> customerJoin = root.join("customer");
            Predicate customerPredicate = criteriaBuilder.equal(customerJoin.get("id"), customerComboBox.getValue().getId());
            predicates.add(customerPredicate);
        }


        if (!staffMultiSelectComboBox.getSelectedItems().isEmpty()) {
            Join<Order, Staff> staffJoin = root.join("staff");
            CriteriaBuilder.In<Long> staffIn = criteriaBuilder.in(staffJoin.get("id"));
            for (Staff staff : staffMultiSelectComboBox.getSelectedItems()) {
                staffIn.value(staff.getId());
            }
            predicates.add(staffIn);
        }


        if (startDate.getValue() != null) {
            Predicate startDatePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate.getValue());
            predicates.add(startDatePredicate);
        }


        if (endDate.getValue() != null) {
            Predicate endDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate.getValue());
            predicates.add(endDatePredicate);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

}
