package my_networked_game.Enums;

public enum MyGameInputType
{
	REGISTER_PLAYER,
	UNREGISTER_PLAYER,
	GENERATE_NEW_TURN, // for use with a player leaving the game
	GAME_BEGIN,
	PLAYER_SKIP,
	PLAYER_SUBMIT,
	PLAYER_ROLL,
	MESSAGE,
	UPDATE_PLAYERS,
	DICE_HOLD_CHANGED
}
