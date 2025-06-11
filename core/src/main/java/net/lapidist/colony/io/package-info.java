/**
 * Utilities for resolving save and configuration paths.
 * <p>
 * The {@link net.lapidist.colony.io.PathService} interface abstracts platform
 * specific logic required to create the game folder and locate save files. The
 * {@link net.lapidist.colony.io.Paths#createDefaultService() default service}
 * selects {@link net.lapidist.colony.io.WindowsPathService} on Windows or
 * {@link net.lapidist.colony.io.UnixPathService} on Unix like systems based on
 * the {@code os.name} system property. Windows saves are stored under the
 * {@code %APPDATA%} directory (falling back to the user home) while Unix systems
 * use {@code ~/.colony}. This ensures both the client and server use the same
 * layout regardless of the operating system.
 */
package net.lapidist.colony.io;
