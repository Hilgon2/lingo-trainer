package com.lingotrainer.application.game.round.turn;

import com.lingotrainer.domain.model.dictionary.Dictionary;
import com.lingotrainer.domain.model.game.Game;
import com.lingotrainer.domain.model.game.GameStatus;
import com.lingotrainer.domain.model.game.round.Round;
import com.lingotrainer.domain.model.game.round.RoundId;
import com.lingotrainer.domain.model.game.round.turn.Turn;
import com.lingotrainer.domain.repository.DictionaryRepository;
import com.lingotrainer.domain.repository.GameRepository;
import com.lingotrainer.domain.repository.RoundRepository;
import com.lingotrainer.domain.repository.TurnRepository;
import com.lingotrainer.application.exception.GameException;
import com.lingotrainer.application.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class BaseTurnService implements TurnService {

    private TurnRepository turnRepository;
    private RoundRepository roundRepository;
    private GameRepository gameRepository;
    private DictionaryRepository dictionaryRepository;

    public BaseTurnService(TurnRepository turnRepository, RoundRepository roundRepository, GameRepository gameRepository, DictionaryRepository dictionaryRepository) {
        this.turnRepository = turnRepository;
        this.roundRepository = roundRepository;
        this.gameRepository = gameRepository;
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    public Optional<Turn> findCurrentTurn(int roundId) {
        return turnRepository.findCurrentTurn(roundId);
    }

    @Override
    public Turn playTurn(int roundId, String guessedWord) {
        Turn currentTurn = this.turnRepository.findCurrentTurn(roundId).orElseThrow(() -> new NotFoundException(String.format("Active turn of round ID %d not found", roundId)));
        Round round = this.roundRepository.findById(currentTurn.getRoundId()).orElseThrow(() -> new NotFoundException(String.format("Round ID %d not found", currentTurn.getRoundId())));

        if (!round.isActive()) {
            throw new GameException(String.format("Round ID %d is not active. Please create a new round", round.getRoundId()));
        }

        // get game and dictionary after active round check
        Game game = this.gameRepository.findById(round.getGameId()).orElseThrow(() -> new NotFoundException(String.format("Game ID %d not found", round.getGameId())));
        Dictionary dictionary = this.dictionaryRepository.findByLanguage(game.getLanguage()).orElseThrow(() -> new NotFoundException(String.format("Dictionary language %s not found", game.getLanguage())));

        currentTurn.setGuessedWord(guessedWord);
        currentTurn.validateTurn(round.getWord(), dictionary);

        this.turnRepository.save(currentTurn);

        boolean roundActive = false;
        if (currentTurn.isCorrectGuess()) {
            game.setScore(game.getScore() + 1);
            this.gameRepository.save(game);
        } else if (!currentTurn.isCorrectGuess() &&
                round.getTurnIds()
                        .stream()
                        .filter(t -> this.turnRepository.findById(t.getId()).orElseThrow(() -> new NotFoundException(String.format("Turn ID %d not found", t.getId()))).getGuessedWord() != null)
                        .count() >= 5) {
            game.setGameStatus(GameStatus.FINISHED);
            this.gameRepository.save(game);

            currentTurn.finishGame();
        } else {
            roundActive = true;
            Turn newTurn = Turn.builder()
                    .startedAt(Instant.now())
                    .roundId(new RoundId(round.getRoundId()))
                    .build();

            this.turnRepository.save(newTurn);
        }

        round.setActive(roundActive);
        this.roundRepository.save(round);

        currentTurn.setGuessedLetters(round.getWord());

        return currentTurn;
    }

    @Override
    public Optional<Turn> findById(int turnId) {
        return this.turnRepository.findById(turnId);
    }
}
