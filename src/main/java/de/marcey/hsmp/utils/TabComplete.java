package de.marcey.hsmp.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TabComplete {

    @Deprecated
    public static List<String> sortDeprecated(String input, List<String> list){
        if(!list.isEmpty() && input != null) {
            Collections.sort(list, new Comparator<String>() {
                public int compare(String s1, String s2) {
                    int pos1 = input.indexOf(s1.charAt(0));
                    int pos2 = input.indexOf(s2.charAt(0));
                    if (pos1 < pos2) {
                        return -1;
                    } else if (pos1 > pos2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            return list;
        }
        return list;
    }

    public static List<String> sortOld(String input, List<String> list) {
        if (input != null && !list.isEmpty()) {
            List<String> filteredList = list.stream()
                    .filter(s -> s.startsWith(input))
                    .collect(Collectors.toList());

            Collections.sort(filteredList, new Comparator<String>() {
                public int compare(String s1, String s2) {
                    int pos1 = input.indexOf(s1.charAt(0));
                    int pos2 = input.indexOf(s2.charAt(0));
                    if (pos1 < pos2) {
                        return -1;
                    } else if (pos1 > pos2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            return filteredList;
        }
        return list;
    }

    public static List<String> sort(String input, List<String> list) {
        if (input != null && !list.isEmpty()) {
            String inputLower = input.toLowerCase();
            List<String> filteredList = list.stream()
                    .filter(s -> s.toLowerCase().startsWith(inputLower))
                    .collect(Collectors.toList());

            Collections.sort(filteredList, new Comparator<String>() {
                public int compare(String s1, String s2) {
                    int pos1 = inputLower.indexOf(s1.toLowerCase().charAt(0));
                    int pos2 = inputLower.indexOf(s2.toLowerCase().charAt(0));
                    if (pos1 < pos2) {
                        return -1;
                    } else if (pos1 > pos2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });

            return filteredList;
        }
        return list;
    }

}
