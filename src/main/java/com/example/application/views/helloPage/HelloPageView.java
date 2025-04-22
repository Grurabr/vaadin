package com.example.application.views.helloPage;

import com.example.application.data.*;
import com.example.application.services.OrderService;
import com.example.application.services.UserService;
import com.example.application.views.broadcaster.Broadcaster;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.shared.ui.Transport;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;


@PageTitle("Welcome")
@Route("")
@AnonymousAllowed
@Menu(order = 0)
public class HelloPageView extends VerticalLayout {
    private final OrderService orderService;
    private final UserService userService;

    private final VerticalLayout messages = new VerticalLayout();
    private final TextField messageField = new TextField();
    private final Button sendButton = new Button("Send");
    Registration broadcasterRegistration;

    public HelloPageView(OrderService orderService, UserService userService) {

        sendButton.addClickListener(e -> {
            Broadcaster.broadcast(messageField.getValue());
            messageField.clear();
        });

        HorizontalLayout sendLayout = new HorizontalLayout(messageField, sendButton);

        this.orderService = orderService;
        this.userService = userService;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated = auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);

        if (!isAuthenticated) {
            H1 header = new H1("Hello in first page");
            H4 text = new H4("Login to start booking");
            Button login = new Button("Login");
            login.addClickListener(e -> UI.getCurrent().navigate("login"));
            add(header, text, login);
            return;
        }

        String username = auth.getName();
        User user = userService.findByEmail(username).orElse(null);

        if (user == null) {
            add(new H4("User not found: " + username));
            return;
        }

        Set<Role> roles = user.getRole();
        String displayName = "";
        List<Order> orders = List.of();

        if (roles.contains(Role.ADMIN)) {
            displayName = "Administrator";
            orders = orderService.getAll();
        } else if (roles.contains(Role.STAFF) && user instanceof Staff staff) {
            displayName = staff.getFullName();
            Specification<Order> spec = (root, query, cb) -> cb.equal(root.get("staff"), staff);
            orders = orderService.getFilteredOrders(spec, 0, 10);
        } else if (roles.contains(Role.USER) && user instanceof Customer customer) {
            displayName = customer.getName();
            Specification<Order> spec = (root, query, cb) -> cb.equal(root.get("customer"), customer);
            orders = orderService.getFilteredOrders(spec, 0, 10);
        } else {
            add(new H4("No role"));
            return;
        }

        add(new H1("Welcome, " + displayName + "!"));

        add(sendLayout, messages); //push

        if (orders.isEmpty()) {
            add(new H4("No bookings found."));
        } else {
            for (Order order : orders) {
                String date = order.getStartDate().toString();
                String operations = order.getOperations().stream()
                        .map(Operation::getName)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("No operations");
                add(new BookingCard(date, operations));
            }
        }

    }

    // Listener
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        broadcasterRegistration = Broadcaster.register(newMessage -> {
            if (ui.isAttached()) {
                ui.access(() -> {
                    messages.add(Notification.show("Your message: " + newMessage, 3000, Notification.Position.TOP_CENTER));
                });
            }
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        if (broadcasterRegistration != null) {
            broadcasterRegistration.remove();
            broadcasterRegistration = null;
        }
    }


}
