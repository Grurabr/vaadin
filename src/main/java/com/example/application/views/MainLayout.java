package com.example.application.views;

import com.example.application.data.Customer;
import com.example.application.data.Staff;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.CustomerService;
import com.example.application.services.StaffService;
import com.example.application.views.login.RegisterComponent;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.checkerframework.checker.units.qual.A;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.contextmenu.MenuItem;

import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    private final AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;
    private final StaffService staffService;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;



    public MainLayout(AuthenticatedUser authenticatedUser,
                      AccessAnnotationChecker accessChecker,
                      StaffService staffService,
                      CustomerService customerService,
                      PasswordEncoder passwordEncoder) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        this.staffService = staffService;
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
        setContent(outerFooter());
    }



    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        Header header = new Header(toggle, viewTitle);
        header.getStyle()
                .set("background-color", "#f5f5f5")
                .set("padding", "0.5em 1em")
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "1em");

        addToNavbar(true, header);
    }

    private void addDrawerContent() {
        Span appName = new Span("My App");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();
        menuEntries.forEach(entry -> {
            if (entry.icon() != null) {
                nav.addItem(new SideNavItem(entry.title(), entry.path(), new SvgIcon(entry.icon())));
            } else {
                nav.addItem(new SideNavItem(entry.title(), entry.path()));
            }
        });

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();



        Div left = new Div();

        Optional<Object> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()){
            Object user = maybeUser.get();

            if(user instanceof Staff){
                Staff staff = (Staff) user;

                //Avatar avatar = new Avatar(staff.getFirstName()+" "+ staff.getLastName());
                //avatar.setThemeName("xsmall");
                //avatar.getElement().setAttribute("tabindex", "-1");

                MenuBar userMenu = new MenuBar();
                userMenu.setThemeName("tertiary-inline contrast");

                MenuItem userName = userMenu.addItem("");
                Div div = new Div();
                //div.add(avatar);
                div.add(staff.getFirstName()+" " + staff.getLastName());
                div.add(new Icon("lumo", "dropdown"));
                div.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.Gap.SMALL);
                userName.add(div);
                userName.getSubMenu().addItem("Sign out", e -> {
                    authenticatedUser.logout();
                });
                left.add(userMenu);

            } else if (user instanceof Customer){
                Customer customer = (Customer) user;

                MenuBar userMenu = new MenuBar();
                userMenu.setThemeName("tertiary-inline contrast");

                MenuItem userName = userMenu.addItem("");
                Div div = new Div();
                //div.add(avatar);
                div.add(customer.getName());
                div.add(new Icon("lumo", "dropdown"));
                div.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.Gap.SMALL);
                userName.add(div);
                userName.getSubMenu().addItem("Sign out", e -> {
                    authenticatedUser.logout();
                });
                left.add(userMenu);
            }

        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            Button registerButton = new Button("Register");
            RegisterComponent registerComponent = new RegisterComponent(customerService, passwordEncoder);
            registerButton.addClickListener(e -> registerComponent.openRegisterComponent());
            left.add(loginLink, registerButton, registerComponent);

        }


        layout.add(left);
        return layout;
    }

    private Footer outerFooter() {
        Footer outerFooder = new Footer();
        outerFooder.getStyle()
                .set("padding", "1em")
                .set("background-color", "#fafafa")
                .set("border-top", "1px solid #ddd")
                .set("text-align", "right")
                .set("flex-shrink", "0");

        Paragraph info = new Paragraph("© 2025 My App Korjamo\n☎ +358 00 123 4567\nemail@a.com");
        info.getStyle()
                .set("white-space", "pre-line");

        outerFooder.add(info);

        return outerFooder;
    }




    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {

        return MenuConfiguration.getPageHeader(getContent()).orElse("");
    }
}
