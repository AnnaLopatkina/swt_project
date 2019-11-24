package missmint.orders.service;

import org.salespointframework.quantity.Metric;
import org.salespointframework.quantity.Quantity;
import org.springframework.data.util.Pair;

import java.util.Map;
import java.util.Set;

public class ServiceConsumptionManager {

	public final static Map<ServiceCategory, Set<Pair<String, Quantity>>> serviceMatRelation = Map.of(
		ServiceCategory.KLUDGE,
		Set.of(
			Pair.of("heel", Quantity.of(2, Metric.UNIT)),
			Pair.of("sole", Quantity.of(2, Metric.UNIT))
		),
		ServiceCategory.SEWING,
		Set.of(
			Pair.of("string", Quantity.of(1, Metric.METER)),
			Pair.of("fabric", Quantity.of(1, Metric.SQUARE_METER))
		),
		ServiceCategory.LOCKSMITH,
		Set.of(Pair.of("blank key", Quantity.of(1, Metric.UNIT))),
		ServiceCategory.CLEANING,
		Set.of(Pair.of("detergent", Quantity.of(10, Metric.LITER))),
		ServiceCategory.ELECTRONICS,
		Set.of(
			Pair.of("soldering wire", Quantity.of(2, Metric.METER)),
			Pair.of("soldering flux", Quantity.of(1, Metric.LITER)),
			Pair.of("screw", Quantity.of(1, Metric.UNIT)),
			Pair.of("nut", Quantity.of(1, Metric.UNIT))
		),
		ServiceCategory.GRINDERY,
		Set.of(Pair.of("sanding paper", Quantity.of(2, Metric.UNIT)))
	);
}
