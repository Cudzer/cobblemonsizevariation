package dev.cudzer.cobblemonsizevariation.command;

public enum PartySlot {
    Slot1(0, "Slot 1"),
    Slot2(1, "Slot 2"),
    Slot3(2, "Slot 3"),
    Slot4(3, "Slot 4"),
    Slot5(4, "Slot 5"),
    Slot6(5, "Slot 6");

    private final int slot;
    private final String displayText;

    PartySlot(int slot, String displayText){
        this.slot = slot;
        this.displayText = displayText;
    }

    public int getSlot(){
        return this.slot;
    }

    public String getDisplayText(){
        return this.displayText;
    }
}
