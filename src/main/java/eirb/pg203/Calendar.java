package eirb.pg203;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Calendar {

    private final List<Entry> items;
    private static final HashMap<String, Function<List<String>, DatePair>> optionsEvents = new HashMap<>();
    private static final HashMap<String, Predicate<Todos>> optionsTodos = new HashMap<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public Calendar() {
        this.items = new ArrayList<>();
    }

    public void addItem(Entry item) {
        this.items.add(item);
    }

    public List<Entry> getItems() {
        return items;
    }

    private void fillOptionsTodos() {
    }

    private void fillOptions() {
        //event options
        optionsEvents.put("today", args -> {
            DatePair pairToday = new DatePair(LocalDate.now().format(formatter), LocalDate.now().format(formatter));
            return pairToday;
        });
        optionsEvents.put("tomorrow", args -> {
            DatePair pairTomorrow = new DatePair(LocalDate.now().plusDays(1).format(formatter), LocalDate.now().plusDays(1).format(formatter));
            return pairTomorrow;
        });
        optionsEvents.put("week", args -> {
            DatePair pairWeek = new DatePair(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(formatter), LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).format(formatter));
            return pairWeek;
        });
        optionsEvents.put("from", args -> {
            String fromDate = args.get(1);
            String toDate = null;
            if (args.contains("to")) {
                int i = args.indexOf("to");
                toDate = args.get(i + 1);
            }
            DatePair pairFrom = new DatePair(fromDate, toDate);
            return pairFrom;
        });
        optionsEvents.put("to", args -> {
            String toDate = args.get(1);
            String fromDate = null;
            if (args.contains("from")) {
                int i = args.indexOf("from");
                fromDate = args.get(i + 1);
            }
            DatePair pairTo = new DatePair(fromDate, toDate);
            return pairTo;
        });

        //todo options
        optionsTodos.put("all", todo -> {
            return false;
        });
        optionsTodos.put("complete", todo -> {
            return todo.getStatus().equals("COMPLETED");
        });
        optionsTodos.put("inprocess", todo -> {
            return todo.getStatus().equals("IN-PROCESS");
        });
        optionsTodos.put("needsaction", todo -> {
            return todo.getStatus().equals("NEEDS-ACTION");
        });
        optionsTodos.put("incomplete", todo -> {
            return !todo.getStatus().equals("COMPLETED");
        });
    }

    public void filterCalendar(List<String> options) {
        if (optionsEvents.isEmpty()) {
            fillOptions();
        }
        if (options.isEmpty()) {
            DatePair today = optionsEvents.get("today").apply(options);
            this.items.removeIf((Entry E) -> {
                if (E instanceof Event) {
                    Event event = (Event) E;
                    return !(event.getDtstart().substring(0, 8).equals(today.getStartDate()));
                }
                return false;
            });
            Predicate<Todos> predicate = optionsTodos.get("incomplete");
            this.items.removeIf((Entry E) -> {
                if (E instanceof Todos) {
                    Todos todo = (Todos) E;
                    return predicate.test(todo);
                }
                return false;
            });
        } else {
            for (String option : options) {
                if (optionsEvents.containsKey(option)) {
                    DatePair datePair = optionsEvents.get(option).apply(options);
                    if (datePair.getStartDate() != null) {
                        this.items.removeIf((Entry E) -> {
                            if (E instanceof Event) {
                                Event event = (Event) E;
                                return (event.getDtstart().substring(0, 8).compareTo(datePair.getStartDate()) < 0);
                            }
                            return false;
                        });
                    }
                    if (datePair.getEndDate() != null) {
                        this.items.removeIf((Entry E) -> {
                            if (E instanceof Event) {
                                Event event = (Event) E;
                                return (event.getDtstart().substring(0, 8).compareTo(datePair.getEndDate()) > 0);
                            }
                            return false;
                        });
                    }
                } else if (optionsTodos.containsKey(option)) {
                    Predicate<Todos> predicate = optionsTodos.get(option);
                    this.items.removeIf((Entry E) -> {
                        if (E instanceof Todos) {
                            Todos todo = (Todos) E;
                            return predicate.test(todo);
                        }
                        return false;
                    });
                }
            }

        }
    }

    public void sortCalendar() {
        if (items.isEmpty()) {
            return;
        }
        if (items.get(0) instanceof Event) {
            items.sort((a, b) -> {
                Event eventA = (Event) a;
                Event eventB = (Event) b;
                return eventA.getDtstart().compareTo(eventB.getDtstart());
            });
        } else if (items.get(0) instanceof Todos) {
            items.sort((a, b) -> {
                Todos todoA = (Todos) a;
                Todos todoB = (Todos) b;
                return todoA.getdueDate().compareTo(todoB.getdueDate());
            });
        }
    }
}
