package be.kdg.model;
/*
 * Xander Van Rompaye
 * 2020-2021
 */
public class Bord {
    Bord(String spelerNaamSpeler, int grootte) {
        speelveldSpeler = new Speelveld(spelerNaamSpeler, false, grootte);
        speelveldCpu = new Speelveld("Cpu", true, grootte);
    }

    private Speelveld speelveldSpeler;
    private Speelveld speelveldCpu;

    public Speelveld getSpeelveldSpeler() {
        return speelveldSpeler;
    }

    public Speelveld getSpeelveldCpu() {
        return speelveldCpu;
    }

    Speelveld getSpeelveldBySpelerNaam(String spelerNaam) {
        if (speelveldSpeler.getSpeler().getNaam().equalsIgnoreCase(spelerNaam))
            return speelveldSpeler;
        if (speelveldCpu.getSpeler().getNaam().equalsIgnoreCase(spelerNaam))
            return speelveldCpu;

        throw new ZeeslagException("Niet mogelijk om een speelveld te vinden voor speler " + spelerNaam);
    }
}
