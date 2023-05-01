package de.marcey.hsmp.utils;

import de.marcey.hsmp.objects.enums.TimeSymbol;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestStart {

    public static void main(String[] args) {

        System.out.println(berechneProzentualenAnteil(2, 1));

    }
    public static double berechneProzentualenAnteil(double wert1, double wert2) {
        if (wert1 > wert2) {
            return 100.0 * wert2 / wert1;
        } else if (wert1 < wert2) {
            return 100.0 * wert1 / wert2;
        } else {
            return 0.0;
        }
    }
    public static List<Integer> getInventorySlots(int numItems) {
        List<Integer> slots = new ArrayList<>();
        int rows = (int) Math.ceil(numItems / 9.0); // Berechne Anzahl der Reihen
        int middleRow = rows / 2; // Bestimme die mittlere Reihe
        int middleSlot = 4; // Bestimme den mittleren Slot in einer Reihe

        if (rows % 2 == 0) {
            middleSlot = 3;
        }

        int firstSlot = middleRow * 9 + middleSlot - ((numItems - 1) / 2 * 2 + 1); // Berechne den ersten Slot in der ersten Reihe

        for (int i = 0; i < numItems; i++) {
            slots.add(firstSlot + i * 2); // Berechne die Slots für jedes Item
        }

        return slots;
    }

}
