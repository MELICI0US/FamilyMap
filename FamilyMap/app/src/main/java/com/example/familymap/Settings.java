package com.example.familymap;

public class Settings {
    private static boolean lifeStoryLines = true;
    private static boolean familyTreeLines = true;
    private static boolean spouseLines = true;
    private static boolean fathersSide = true;
    private static boolean mothersSide = true;
    private static boolean maleEvents = true;
    private static boolean femaleEvents = true;

    public boolean isLifeStoryLines() {
        return lifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean isFamilyTreeLines() {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean isFathersSide() {
        return fathersSide;
    }

    public void setFathersSide(boolean fathersSide) {
        this.fathersSide = fathersSide;
    }

    public boolean isMothersSide() {
        return mothersSide;
    }

    public void setMothersSide(boolean mothersSide) {
        this.mothersSide = mothersSide;
    }

    public boolean isMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }

    public boolean isFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }
}
