// /////////////////////////////////////////////////////////
// This file is part of Propel.
//
// Propel is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Propel is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with Propel. If not, see <http://www.gnu.org/licenses/>.
// /////////////////////////////////////////////////////////
// Authored by: Nikolaos Tountas -> salam.kaser-at-gmail.com
// /////////////////////////////////////////////////////////
package propel.core.transactional;

import java.util.List;
import lombok.Functions.Function0;

/**
 * The interface of the transaction manager, capable of running actions in a transactional fashion.
 */
public interface ITransactionManager
{
  /**
   * A list of actions, executed in a way that guarantees complete success or complete failure.
   */
  List<Function0<Void>> getActions();

  /**
   * If an action fails, the corresponding rollback actions are executed in FIFO or FILO order, as configured by the caller.
   */
  List<Function0<Void>> getRollbackActions();

  /**
   * The index of the action that is executing. This is useful for determining the index of any actions that fail. This can be then used to
   * call the right rollback action.
   */
  int getExecutionIndex();

  /**
   * Re-initializes the manager to allow for re-use
   */
  void clear();

  /**
   * Adds an action without a corresponding rollback action. This should only be used for actions that do not alter things.
   */
  void add(Function0<Void> action);

  /**
   * Adds an action and the corresponding rollback action. If the transaction fails before all actions have been successfully completed then
   * the rollback actions are called, so ensure that the rollback action acts as an "undo" operation for the action.
   */
  void add(Function0<Void> action, Function0<Void> rollbackAction);

  /**
   * Executes all actions, starting at the beginning. If an exception occurs during the execution of an action then it is thrown.
   * 
   * @throws Throwable If an exception occurs during the commit stage.
   */
  void commit()
      throws Throwable;

  /**
   * Rolls back transactions from the point where they were left. Call this method if the Commit has failed. If an exception is thrown
   * during rollback then the the exception is thrown.
   * 
   * @throws Throwable If an exception occurs during the commit stage.
   */
  void rollback()
      throws Throwable;

  /**
   * Executes all actions, starting at the beginning. If an exception occurs during the execution of an action, then a rollback takes place.
   * The original exception is then thrown. If an exception is throw during rollback, then the actions are not fully reverted and the
   * original exception is thrown, while the rollback exception is logged.
   */
  void commitWithRollback()
      throws Throwable;

  /**
   * Skips the next commit step. Can be used if the Commit has thrown an exception to increment the executed action index so that the commit
   * resumes at the next step, avoiding the same error from happening for the second time.
   * 
   * @throws IllegalStateException When there is no other commit step to skip
   */
  void skipCommitStep();

  /**
   * Skips the next rollback step. Can be used if the Commit has thrown an exception to decrement the executed action index so that the
   * rollback resumes at the next step, avoiding the same error from happening for the second time.
   * 
   * @throws IllegalStateException When there is no other rollback step to skip
   */
  void skipRollbackStep();

  /**
   * Executes all actions, starting at the execution index until the end. If an exception occurs during the execution of an action then the
   * exception is thrown.
   * 
   * @throws IndexOutOfBoundsException When the execution index is out of range
   * @throws Throwable If an exception occurs during transactions
   */
  void resumeCommit()
      throws Throwable;

  /**
   * Rolls back all actions, starting at the last failed action index until the first action. If an exception is thrown during rollback then
   * the the exception is thrown.
   * 
   * @throws IndexOutOfBoundsException When the execution index is out of range
   * @throws Throwable If an exception occurs during rollback
   */
  void resumeRollback()
      throws Throwable;
}
