package com.example.application.views;

import com.example.application.data.Customer;
import com.example.application.data.Staff;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.CustomerService;
import com.example.application.services.StaffService;
import com.example.application.views.login.RegisterComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.WebStorage;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
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
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout implements LocaleChangeObserver {

    private H1 viewTitle = new H1();

    private final AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;
    private final StaffService staffService;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    private final I18NProvider provider;
    private Span appName;
    private Anchor loginLink = new Anchor();
    private Button registerButton = new Button();
    private Scroller scroller = new Scroller();



    public MainLayout(AuthenticatedUser authenticatedUser,
                      AccessAnnotationChecker accessChecker,
                      StaffService staffService,
                      CustomerService customerService,
                      PasswordEncoder passwordEncoder,
                      I18NProvider provider) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        this.staffService = staffService;
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.provider = provider;

        WebStorage.getItem("locale", value -> {
            if (value != null)
                UI.getCurrent().setLocale(Locale.forLanguageTag(value));
        });

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }


    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        Select<Locale> langSelect = new Select<>();

        List<Locale> locales = provider.getProvidedLocales();
        langSelect.setItems(locales);
        langSelect.setItemLabelGenerator(Locale::getDisplayLanguage);

        WebStorage.getItem("locale", savedLocaleTag -> {
            if (savedLocaleTag != null) {
                locales.stream()
                        .filter(locale -> locale.toLanguageTag().equals(savedLocaleTag))
                        .findFirst()
                        .ifPresent(langSelect::setValue);
            } else {
                langSelect.setValue(UI.getCurrent().getLocale());
            }
        });

        langSelect.addValueChangeListener(event -> {
            Locale selectedLocale = event.getValue();
            if (selectedLocale != null) {
                UI.getCurrent().setLocale(selectedLocale);
                WebStorage.setItem("locale", selectedLocale.toLanguageTag());
            }
        });



        Header header = new Header(toggle, viewTitle, langSelect);
        header.getStyle()
                .set("background-color", "#f5f5f5")
                .set("padding", "0.5em 1em")
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "1em");

        addToNavbar(true, header);
    }

    private void addDrawerContent() {
        appName = new Span(getTranslation("myapp"));
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        scroller.setContent(createNavigation());

        addToDrawer(header, scroller, createFooter(), outerFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        List<MenuEntry> menuEntries = MenuConfiguration.getMenuEntries();
        menuEntries.forEach(entry -> {
            String translate = getTranslation(entry.title());
            String finalTitle = translate.equals(entry.title()) ? entry.title() : translate;
            if (entry.icon() != null) {
                nav.addItem(new SideNavItem(finalTitle, entry.path(), new SvgIcon(entry.icon())));
            } else {
                nav.addItem(new SideNavItem(finalTitle, entry.path()));
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
            loginLink.setHref("login");
            loginLink.setText(getTranslation("signInButton"));
            registerButton.setText(getTranslation("regButton"));
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

        Paragraph info = new Paragraph("© 2025 My App Korjamo\n +358 00 123 4567\nemail@a.com");
        info.getStyle()
                .set("white-space", "pre-line");

        outerFooder.add(info);

        return outerFooder;
    }




    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getTranslation(getCurrentPageTitle()));
    }

    private String getCurrentPageTitle() {

        return MenuConfiguration.getPageHeader(getContent()).orElse("");
    }

    @Override
    public void localeChange(LocaleChangeEvent localeChangeEvent){

        appName.setText(getTranslation("myapp"));
        viewTitle.setText(getCurrentPageTitle());

        loginLink.setText(getTranslation("signInButton"));
        registerButton.setText(getTranslation("regButton"));
        afterNavigation();
        scroller.setContent(createNavigation());

    }
}
