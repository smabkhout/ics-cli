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

    private void fillOptions() {
        
        //event options
        optionsEvents.put("-today", args -> {
            DatePair pairToday = new DatePair(LocalDate.now().format(formatter), LocalDate.now().format(formatter));
            return pairToday;
        });
        optionsEvents.put("-tomorrow", args -> {
            DatePair pairTomorrow = new DatePair(LocalDate.now().plusDays(1).format(formatter), LocalDate.now().plusDays(1).format(formatter));
            return pairTomorrow;
        });
        optionsEvents.put("-week", args -> {
            DatePair pairWeek = new DatePair(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(formatter), LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).format(formatter));
            return pairWeek;
        });
        optionsEvents.put("-from", args -> {
            int j = args.indexOf("-from");
            String fromDate = args.get(j+1);
            String toDate = null;
            if (args.contains("-to")) {
                int i = args.indexOf("-to");
                toDate = args.get(i + 1);
            }
            DatePair pairFrom = new DatePair(fromDate, toDate);
            return pairFrom;
        });
        optionsEvents.put("-to", args -> {
            int j = args.indexOf("-to");
            String toDate = args.get(j+1);
            String fromDate = null;
            if (args.contains("-from")) {
                int i = args.indexOf("-from");
                fromDate = args.get(i + 1);
            }
            DatePair pairTo = new DatePair(fromDate, toDate);
            return pairTo;
        });

        //todo options
        optionsTodos.put("-all", todo -> false );
        optionsTodos.put("-completed", todo -> !todo.getStatus().equals("COMPLETED"));
        optionsTodos.put("-inprocess", todo -> !todo.getStatus().equals("IN-PROCESS"));
        optionsTodos.put("-needsaction", todo -> !todo.getStatus().equals("NEEDS-ACTION"));
        optionsTodos.put("-incomplete", todo -> todo.getStatus().equals("COMPLETED"));
    }

    private void todosFilter(String opt){

        Predicate<Todos> predicate = optionsTodos.get(opt);
            this.items.removeIf((Entry E) -> {
                if (E instanceof Todos) {
                    Todos todo = (Todos) E;
                    return predicate.test(todo);
                }
                return false;
            });
    }

    private void eventsFilter(String opt, List<String> options) {
        DatePair datePair = optionsEvents.get(opt).apply(options);
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
    }

    public void filterCalendar(List<String> options) {
        if (optionsEvents.isEmpty()) {
            fillOptions();
        }

        if (options.contains("events")) {
            this.items.removeIf(E -> !(E instanceof Event));
        } else if (options.contains("todos")) {
            this.items.removeIf(E -> !(E instanceof Todos));
        }

        // for default behaviour
        boolean hasEventFilter = false;
        boolean hasTodoFilter = false;

        for (String opt : options) {
            if (optionsEvents.containsKey(opt)) hasEventFilter = true;
            if (optionsTodos.containsKey(opt)) hasTodoFilter = true;
        }

         if (!hasTodoFilter) {
            todosFilter("-incomplete");
        }

        if (!hasEventFilter) {
            eventsFilter("-today", options);
        }    
       


        for (String option : options) {
                if (optionsEvents.containsKey(option)) {
                    eventsFilter(option, options);
                } else if (optionsTodos.containsKey(option)) {
                    todosFilter(option);
                }
            }

    }
    

    public void sortCalendar() {
        if (items.isEmpty()) {
            return;
        }
        items.sort((a, b) -> {
            String dateA;
            String dateB;
            if (a instanceof Event) {
                dateA = ((Event) a).getDtstart();
            } else {
                dateA = ((Todos) a).getDueDate();
            }
            if (b instanceof Event) {
                dateB = ((Event) b).getDtstart();
            } else {
                dateB = ((Todos) b).getDueDate();
            }
            if (dateA == null) return 1;
            if (dateB == null) return -1;
            
            return dateA.compareTo(dateB);
        });
    }
}
