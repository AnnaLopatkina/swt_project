package missmint.time;

import missmint.orders.order.MissMintOrder;
import missmint.rooms.Room;
import missmint.users.model.Staff;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entry which represents a booked time slot
 */
@Entity
public class TimeTableEntry {
	@Id
	@GeneratedValue
	private long id;
	@OneToOne
	@JoinColumn(name = "missmintorder")
	private MissMintOrder order;
	@ManyToOne
	private Staff staff;
	private int slot;
	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	@ManyToOne
	private Room room;
	private LocalDate date;

	public TimeTableEntry(MissMintOrder order, Staff staff, int slot, Room room, LocalDate date) {
		this.order = order;
		this.staff = staff;
		this.slot = slot;
		this.room = room;
		this.date = date;
	}

	public TimeTableEntry() {
	}

	public long getId() {
		return id;
	}

	public MissMintOrder getOrder() {
		return order;
	}

	public LocalDate getDate() {
		return date;
	}

	/**
	 * @return The time the work on the customer item for this slot will be started.
	 */
	public LocalDateTime getBeginning() {
		return LocalDateTime.of(date, TimeTableService.SLOTS.get(slot).getFirst());
	}

	/**
	 * @return The time the work on the customer item for this slot will be finished.
	 */
	public LocalDateTime getEnd() {
		return LocalDateTime.of(date, TimeTableService.SLOTS.get(slot).getSecond());
	}

	public Staff getStaff() {
		return staff;
	}

	public void setOrder(MissMintOrder order) {
		this.order = order;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TimeTableEntry)) return false;
		TimeTableEntry that = (TimeTableEntry) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
