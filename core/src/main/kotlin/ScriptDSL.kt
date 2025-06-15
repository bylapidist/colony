import net.lapidist.colony.events.Events
import net.mostlyoriginal.api.event.common.Event
import net.mostlyoriginal.api.event.common.Subscribe

inline fun <reified T : Event> on(noinline handler: (T) -> Unit) {
    Events.getInstance().registerEvents(
        object {
            @Subscribe
            fun handle(event: T) {
                handler(event)
            }
        },
    )
}
