package io.infinite.orbit

import groovy.swing.SwingBuilder
import io.infinite.blackbox.BlackBox
import io.infinite.carburetor.CarburetorLevel

import javax.swing.*

@BlackBox(level = CarburetorLevel.METHOD)
class GroovyMainForm {

    SwingBuilder swing = new SwingBuilder()

    Integer count = 0

    Closure<JPanel> sharedPanel = {
        swing.panel() {
            label("Shared Panel")
        }
    }

    void show() {
        SwingBuilder.lookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")
        swing.edt {
            frame(
                    title: 'Frame',
                    defaultCloseOperation: JFrame.EXIT_ON_CLOSE,
                    pack: true,
                    show: true
            ) {
                vbox() {
                    textlabel = label('Click the button!')
                    button(
                            text: 'Click Me',
                            actionPerformed: {
                                count++
                                textlabel.text = "Clicked ${count} time(s)."
                                println "Clicked!"
                            }
                    )
                    widget(sharedPanel())
                    widget(sharedPanel())
                }
            }
        }
    }

}
