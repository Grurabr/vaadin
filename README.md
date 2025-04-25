# My App

Tein verkkosivuston auton korjauksen varaamista varten

## Data ja entiteetit

Järjestelmässä on neljä entiteettiä: Staff (OneToMany Orders), Customer (OneToMany Orders)
ja Operations (ManyToMany Orders). Kaikki perivät luokan AbstractEntity, ja lisäksi Staff ja
Customer perivät myös User-luokan. Sivustolle voi kirjautua seuraavasti:
admin@a.com/admin, staff@a.com/staff ja customer@a.com/customer.
Uuden Customerin voi luoda rekisteröitymisen kautta, ja uuden Staffin tai Adminin sivun /new-staff kautta. 
Tietoja voi luoda, muokata ja poistaa sivulla /edit-page. 
Testaamista varten suosittelen luomaan uusia käyttäjiä, sillä olemassa olevat ovat sidoksissa tilauksiin, 
ja ennen kuin ne poistetaan, täytyy kaikki niihin liittyvät tilaukset poistaa.

## Suodattimet

Toteutin suodattimen sivulle /edit-page valittaessa 'orders'. 
Tilausten suodatus on mahdollista ID:n, asiakkaan ja 
työntekijöiden mukaan – työntekijöitä voi valita useamman kerralla. 
Lisäksi voi suodattaa aloituspäivän, lopetuspäivän tai aikavälin mukaan.

## Tyylit
esimerkiksi HelloPageView.java rivi 74
MainLayout.java rivi 230-235 
BookingCard.java / frontend/themes/my-app/components/card-theme.css
frontend/styles/global-styles.css @CssImport("./styles/global-styles.css")

## Ulkoasu
SPA-sovellus, jossa on päänäkymä - On

Header - On

Toimiva navigointipalkki - On

Footer - On

Selkeästi erityyppisiä sisältösivuja vähintään kolme kappaletta. Edelliset pitää toimia näiden kanssa

1. NewStaffView - Form
2. EditPageView - Table
3. HelloPage - Cards of orders
4. AccessDeniedView
5. LoginView/RegisterComponent

## Autentikointi ja tietoturva
Security-palikan käyttöönotto - On

Sisäänkirjautumissivun luominen - On

Käyttäjäentiteetin luominen ja roolien määrittäminen (Admin/user) - On

Toteuta: - Kaikki käyttäjät näkevät päänäkymän - User 
ja Admin käyttäjät näkevät jonkun sivun - Sivu pelkästään ADMIN käyttäjille - On, 
Admin näkee kaikki sivut, Staff ja User vain HelloPageView

Kustomoitu virheviesti jos user yrittää admin-sivulle - 
Tehty /access-denied, Staff ei pääse /edit-page:lle ja /new-staff:lle

## Julkaise työ GIT:iin
Tehty

## Salasanojen salaus jollain menetelmällä
org.springframework.security.crypto.password.PasswordEncoder

## Toteuta sovelluksessasi server push
En täysin ymmärtänyt tehtävänantoa, mutta toteutin broadcast toiminnon, 
joka lähettää push-viestin kaikille.
HelloPageView-sivulla on Send-painike tätä varten.

## Toteuta lokalisointi vähintään yhdellä sivulla
MainLayout - select headerissa
