import net.lapidist.colony.events.Events
import net.mostlyoriginal.api.event.common.Event

inline fun <reified T : Event> on(noinline handler: (T) -> Unit) {
    Events.listen(T::class.java) { event -> handler(event) }
}
