package ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;

import model.Event;
import model.EventLog;

public class SwingWindowListener implements WindowListener {
    @Override
    public void windowOpened(WindowEvent e) {
        // Do nothing
    }

    @Override
    public void windowClosing(WindowEvent e) {
       // Do nothing
    }

    @Override
    public void windowClosed(WindowEvent e) {
        Iterator<Event> iterator = EventLog.getInstance().iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            System.out.println(event.getDescription());
        }        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // Do nothing
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // Do nothing
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // Do nothing
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // Do nothing
    }
}
