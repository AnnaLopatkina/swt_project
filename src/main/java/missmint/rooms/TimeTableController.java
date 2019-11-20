package missmint.rooms;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Controller
public class TimeTableController {

	private RoomsRepository rooms;
	private EntriesRepository entries;
	private Set<TimeTableEntry> existingEntries;

	@PostMapping("/rooms/{id}/showEntries")
	public String showEntries(Model model, @PathVariable("id") long id){
		existingEntries= new HashSet<>();

		//TODO entries existieren nur, wenn gebucht worden ist
		rooms.findById(id).map(room -> {	//nimmt Wert falls nicht leer, dann wird funktion delete aufgerufen
				for (TimeTableEntry entry : room.getEntrySet())	{
					if (entry.getOrder().getInbound().equals(LocalDate.now())){
						existingEntries.add(entry);
					}
				}
				return 1;
			}
		);
		model.addAttribute("rooms", rooms.findAll());
		model.addAttribute("entries", entries.findAll());
		model.addAttribute("existingEntries", existingEntries);

		return "entries";
	}
}
