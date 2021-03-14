package be.kdg.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class Speelveld {
    private int grootte;
    private Speler speler;
    private List<Vak> vakken;
    private List<Schip> schepen;
    private Random random;

    Speelveld(String spelerNaam, boolean isCpu, int grootte) {
        this.grootte = grootte;
        initialiseerVakken();
        initialiseerSchepen();
        speler = new Speler(spelerNaam, isCpu);
    }

    public int getGrootte() {
        return grootte;
    }

    public Speler getSpeler() {
        return speler;
    }

    // Geeft de status van een vak terug
    public StatusVak getStatusVak(String positie) {
        for (Vak vak : vakken) {
            if (vak.getPositie().equals(positie)) {
                return vak.getStatus();
            }
        }

        return null;
    }

    // Geeft de kleur van een vak terug
    public Color getSchipKleur(String positie) {
        for (Vak vak : vakken) {
            if (vak.getPositie().equals(positie) && vak.getSchip() != null) {
                return vak.getSchip().getKleur();
            }
        }
        return null;
    }

    // Alle vakken worden geinitialiseerd en in de List vakken geplaatst
    // Een vak heeft een String positie (Bv. 4E) --> 4 = rij, E = kolom
    private void initialiseerVakken() {
        vakken = new ArrayList<Vak>();

        for (int rij = 1; rij <= grootte; rij++) {
            for (int kolom = 1; kolom <= grootte; kolom++) {
                //Vb. maak 2B
                String positie = rij + Character.toString(kolom + 64);

                Vak vak = new Vak(positie);

                vakken.add(vak);
            }
        }
    }

    // Alle schepen worden geinitialiseerd en in de List schepen geplaatst
    private void initialiseerSchepen() {
        schepen = new ArrayList<Schip>();

        schepen.add(new Schip("vliegdekschip", 1, 6));
        schepen.add(new Schip("torpedo", 1, 4));
        schepen.add(new Schip("torpedo", 2, 4));
        schepen.add(new Schip("slagschip", 1, 3));
        schepen.add(new Schip("slagschip", 2, 3));
        schepen.add(new Schip("slagschip", 3, 3));
        schepen.add(new Schip("patrouilleschip", 1, 2));
        schepen.add(new Schip("patrouilleschip", 2, 2));
        schepen.add(new Schip("patrouilleschip", 3, 2));
        schepen.add(new Schip("patrouilleschip", 4, 2));
    }

    // Deze methode geeft alle vakken voor een bepaalde rij terug
    private List<Vak> getVakkenVoorRij(int rij) {
        List<Vak> vakkenVoorRij = new ArrayList<Vak>();

        for (Vak vak : vakken) {
            if (vak.getRij() == rij) {
                vakkenVoorRij.add(vak);
            }
        }
        return vakkenVoorRij;
    }

    // Deze methode geeft alle vakken voor een bepaalde kolom terug
    private List<Vak> getVakkenVoorKolom(int kolom) {
        List<Vak> vakkenVoorKolom = new ArrayList<Vak>();

        for (Vak vak : vakken) {
            if (vak.getKolom() == kolom) {
                vakkenVoorKolom.add(vak);
            }
        }
        return vakkenVoorKolom;
    }

    // Deze methode plaatst een schip op een vak --> vak.Statusvak == gevuld
    void plaatsSchip(String schipNaam, int id, String positie, boolean isHorizontaal) {
        // Valideren of schip bestaat
        Schip tePlaatsenSchip = null;
        for (Schip schip : schepen) {
            if (schip.getNaam().equals(schipNaam) && schip.getId() == id) {
                tePlaatsenSchip = schip;
                break;
            }
        }

        // Het schip bestaat niet
        if (tePlaatsenSchip == null)
            throw new ZeeslagException("Kan schip met ongeldige naam " + schipNaam + " niet positioneren");

        // Rij positie valideren
        int rij = Vak.getRijVanPositie(positie);
        if (rij < 1 || rij > grootte)
            throw new ZeeslagException("Rij waarde " + rij + " van positie " + positie + " is ongeldig, deze moet van 1 tot 10 zijn");

        // Kolom positie valideren
        int kolom = Vak.getKolomVanPositie(positie);
        if (kolom < 1 || kolom > grootte)
            throw new ZeeslagException("Kolom waarde " + kolom + " van positie " + positie + " is ongeldig, deze moet van A tot Z zijn");

        // Is het schip plaatsbaar op het speelveld?
        if (isHorizontaal) {
            if (kolom + tePlaatsenSchip.getLengte() - 1 > grootte)
                throw new ZeeslagException("Niet mogelijk om " + schipNaam + " met lengte " + tePlaatsenSchip.getLengte() + " op positie " + positie + " te plaatsen");
        } else {
            if (rij + tePlaatsenSchip.getLengte() - 1 > grootte)
                throw new ZeeslagException("Niet mogelijk om " + schipNaam + " met lengte " + tePlaatsenSchip.getLengte() + " op positie " + positie + " te plaatsen");
        }

        // Plaats schip horizontaal
        if (isHorizontaal) {
            List<Vak> vakkenVoorRij = getVakkenVoorRij(rij);
            for (Vak vak : vakkenVoorRij) {
                if (vak.getKolom() >= kolom && vak.getKolom() < kolom + tePlaatsenSchip.getLengte()) {
                    if (vak.getStatus() == StatusVak.gevuld)
                        throw new ZeeslagException("Niet mogelijk om schip " + schipNaam + " te plaatsen op al ingenomen plaats " + positie);

                    vak.setSchip(tePlaatsenSchip, isHorizontaal);
                }
            }
        }
        // Plaats schip Verticaal
        else {
            List<Vak> vakkenVoorKolom = getVakkenVoorKolom(kolom);
            for (Vak vak : vakkenVoorKolom) {
                if (vak.getRij() >= rij && vak.getRij() < rij + tePlaatsenSchip.getLengte()) {
                    if (vak.getStatus() == StatusVak.gevuld)
                        throw new ZeeslagException("Niet mogelijk om schip " + schipNaam + " te plaatsen op al ingenomen plaats " + positie);

                    vak.setSchip(tePlaatsenSchip, isHorizontaal);
                }
            }
        }
    }

    public void draaiSchip(String positie) {
        Vak vak = getVak(positie);
        if (vak == null || vak.getSchip() == null)
            return;

        // Waarden onthouden vooraleer we het schip verwijderen van het bord
        String eerstePositieVanSchip = zoekEerstePositieVanSchip(vak.getSchip());
        String schipNaam = vak.getSchip().getNaam();
        int id = vak.getSchip().getId();
        boolean isHorizontal = vak.hasHorizontalShip();

        try {
            // Verwijder schip van bord en voeg terug toe in andere richting
            verwijderSchip(vak.getSchip());
            plaatsSchip(schipNaam, id, eerstePositieVanSchip, !isHorizontal);
        } catch (ZeeslagException ze) {
            // Niet mogelijk om het schip te draaien, zet terug naar oorspronkelijke positie
            plaatsSchip(schipNaam, id, eerstePositieVanSchip, isHorizontal);
        }
    }

    // Methode om een schip op een vak te bepalen
    public Schip getSchip(String positie) {
        Vak vak = getVak(positie);
        if (vak == null || vak.getSchip() == null)
            return null;

        return vak.getSchip();
    }

    // Methode om een schip van een vak te verwijden
    public void verwijderSchip(Schip schip) {
        for (Vak vak : vakken) {
            Schip schipOpVak = vak.getSchip();
            if (schipOpVak == null)
                continue;

            if (schipOpVak.getNaam().equals(schip.getNaam()) && schipOpVak.getId() == schip.getId())
                vak.removeSchip();
        }
    }

    // Deze methode geeft de eerste positie van een schip terug
    private String zoekEerstePositieVanSchip(Schip schip) {
        for (Vak vak : vakken) {
            Schip schipOpVak = vak.getSchip();
            if (schipOpVak == null)
                continue;

            if (schipOpVak.getNaam().equals(schip.getNaam()) && schipOpVak.getId() == schip.getId())
                return vak.getPositie();
        }

        throw new ZeeslagException("Eerste positie van schip " + schip.getNaam() + " kan niet gevonden worden");
    }

    private Vak getVak(String positie) {
        for (Vak vak : vakken) {
            if (vak.getPositie().equals(positie)) {
                return vak;
            }
        }

        return null;
    }

    AanvalResultaat valAan(String positie) {
        Vak aangevallenVak = getVak(positie);

        if (aangevallenVak == null)
            throw new ZeeslagException("Niet mogelijk om vak met positie " + positie + " aan te vallen");

        boolean geraakt = aangevallenVak.valAan();

        // Aanval gemist
        if (geraakt)
            return AanvalResultaat.raak;

        return AanvalResultaat.mis;
    }

    void genereerSchepenCpu() {
        while (!schepen.isEmpty()) {
            random = new Random();
            boolean isHorizontaal = Math.random() < 0.5;

            // Maakt een postitie bestaande uit een random rij en een random kolom
            String positie = (random.nextInt(grootte) + 1) + String.valueOf((char) (random.nextInt(grootte) + 'A'));

            // Te plaatsen schip bepalen
            Schip cpuSchip = schepen.get(0);

            try {
                plaatsSchip(cpuSchip.getNaam(), cpuSchip.getId(), positie, isHorizontaal);
                schepen.remove(cpuSchip);
            } catch (ZeeslagException e) {
            }
        }
    }

    private Random randomAanvalPositieCpu = new Random();

    // Methode om een vak te kiezen dat de CPU moet aanvallen
    // TODO dit algoritme intelligenter maken (bv. buurvakken aanvallen indien vorige aanval succesvol was)
    static String bepaalAanTeVallenPositieCpu(Speelveld speelveld) {
        Vak aanTeVallenVak = null;
        int pogingen = 0;

        while (pogingen < 200) {
            pogingen++;

            // Random.nextInt = traag! https://www.baeldung.com/java-thread-local-random
            aanTeVallenVak = speelveld.vakken.get(ThreadLocalRandom.current().nextInt(0, (speelveld.getGrootte() * speelveld.getGrootte()) - 1));

            // Op een gevuld vak staat een (stuk) boot dat nog niet geraakt is
            if (aanTeVallenVak.getStatus() == StatusVak.gevuld || aanTeVallenVak.getStatus() == StatusVak.leeg) {
                return aanTeVallenVak.getPositie();
            }
        }

        // Geen vrij vak kunnen vinden via generatie, we zoeken het eerste vrije vak
        for (int i = 0; i < speelveld.vakken.size(); i++) {
            Vak vak = speelveld.vakken.get(i);
            if (vak.getStatus() == StatusVak.gevuld)
                return vak.getPositie();
        }

        // Kan nooit
        return speelveld.vakken.get(0).getPositie();
    }

    // Deze methode geeft true terug als alle schepen geraakt zijn
    boolean zijnAlleSchepenGeraakt() {
        for (Vak vak : vakken) {
            if (vak.getStatus() == StatusVak.gevuld) {
                return false;
            }
        }
        return true;
    }
}
