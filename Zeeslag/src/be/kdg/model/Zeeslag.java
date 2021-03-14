package be.kdg.model;
/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class Zeeslag {
    private Bord bord;
    private int aantalBeurten;

    public Bord getBord() {
        return bord;
    }

    public void startSpel(String spelerNaam, int grootte) {
        bord = new Bord(spelerNaam, grootte);
    }

    public void plaatsSchip(String spelerNaam, String schipNaam, int id, String positie, boolean isHorizontaal) {
        Speelveld speelveld = bord.getSpeelveldBySpelerNaam(spelerNaam);

        speelveld.plaatsSchip(schipNaam, id, positie, isHorizontaal);
    }

    public void genereerSchepenCpu() {
        Speelveld speelveld = bord.getSpeelveldCpu();
        speelveld.genereerSchepenCpu();
    }

    public AanvalResultaat valAan(String positie) {
        // Het aantal spelbeurten wordt verhoogd
        aantalBeurten++;

        // Speler valt aan (via klik op veld van CPU)
        AanvalResultaat aanvalResultaatSpeler = bord.getSpeelveldCpu().valAan(positie);

        // Vervolgens valt de CPU aan
        String aanvalPositieVoorCpu = Speelveld.bepaalAanTeVallenPositieCpu(bord.getSpeelveldSpeler());
        bord.getSpeelveldSpeler().valAan(aanvalPositieVoorCpu);

        return aanvalResultaatSpeler;
    }

    public int getAantalBeurten() {
        return aantalBeurten;
    }

    public Speler getWinnaar() {
        // Als alle schepen van de speler zijn geraakt --> Cpu is winnaar
        if (bord.getSpeelveldSpeler().zijnAlleSchepenGeraakt())
            return bord.getSpeelveldCpu().getSpeler();

        // Als alle schepen van de Cpu zijn geraak --> speler is winnaar
        if (bord.getSpeelveldCpu().zijnAlleSchepenGeraakt())
            return bord.getSpeelveldSpeler().getSpeler();

        return null;
    }

    public Speler getSpeler() {
        return bord.getSpeelveldSpeler().getSpeler();
    }
}
