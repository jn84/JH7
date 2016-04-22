package my_networked_game.Interfaces;

import java.util.EventListener;

import my_networked_game.HelperClasses.SelectableTextFieldEvent;

public interface SelectableTextFieldListener extends EventListener
{
	void handleSelectableTextFieldEvent(SelectableTextFieldEvent event);
}
