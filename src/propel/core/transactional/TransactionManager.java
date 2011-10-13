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

import propel.core.common.CONSTANT;
import propel.core.functional.projections.Projections;
import propel.core.utils.ExceptionUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.Functions.Function0;

/**
 * Allows for actions to be executed in a transactional manner, i.e. guarantees that all actions run to completion or rolls back committed
 * actions if an exception is thrown.
 */
public class TransactionManager
    implements ITransactionManager
{
  /**
   * A list of actions, executed in a way that guarantees complete success or complete failure.
   */
  private List<Function0<Void>> actions;
  /**
   * If an action fails, the corresponding rollback actions are executed
   */
  private List<Function0<Void>> rollbackActions;
  /**
   * The index of the action that is executing. This is useful for determining the index of any actions that fail. This can be then used to
   * call the right rollback action.
   */
  private int executionIndex;

  /**
   * Default constructor
   */
  public TransactionManager()
  {
    clear();
  }

  /**
   * {@inheritDoc}
   */
  public void clear()
  {
    actions = new ArrayList<Function0<Void>>(32);
    rollbackActions = new ArrayList<Function0<Void>>(32);
    executionIndex = 0;
  }

  /**
   * {@inheritDoc}
   */
  public void add(Function0<Void> action)
  {
    add(action, null);
  }

  /**
   * {@inheritDoc}
   */
  public void add(Function0<Void> action, Function0<Void> rollbackAction)
  {
    if (action == null)
      action = Projections.emptyFunc();
    if (rollbackAction == null)
      rollbackAction = Projections.emptyFunc();

    actions.add(action);
    rollbackActions.add(rollbackAction);
  }

  /**
   * {@inheritDoc}
   */
  public void commit()
  {
    // execute all (from start)
    for (executionIndex = 0; executionIndex < actions.size(); executionIndex++)
      actions.get(executionIndex).apply();
  }

  /**
   * {@inheritDoc}
   */
  public void rollback()
  {
    // execute in LIFO style
    for (executionIndex--; executionIndex >= 0; executionIndex--)
      rollbackActions.get(executionIndex).apply();
  }

  /**
   * {@inheritDoc}
   */
  public void commitWithRollback()
      throws Throwable
  {
    try
    {
      commit();
    }
    catch(Throwable e)
    {
      try
      {
        rollback();
      }
      catch(Throwable e2)
      {
        // LLTODO:
        // PropelLog.error("Rollback failed to complete: " + e2);
        throw new Throwable("Rollback failed to complete: " + ExceptionUtils.getMessages(e2, CONSTANT.ENVIRONMENT_NEWLINE)
            + CONSTANT.ENVIRONMENT_NEWLINE + "Original exception: ", e);
      }
      throw e;
    }
  }

  /**
   * {@inheritDoc}
   */
  public void skipCommitStep()
  {
    if (executionIndex < actions.size())
      executionIndex++;
    else
      throw new IllegalStateException("There is no other commit step to skip.");
  }

  /**
   * {@inheritDoc}
   */
  public void skipRollbackStep()
  {
    if (executionIndex > 0)
      executionIndex--;
    else
      throw new IllegalStateException("There is no other rollback step to skip.");
  }

  /**
   * {@inheritDoc}
   */
  public void resumeCommit()
  {
    if (executionIndex < 0 || executionIndex >= rollbackActions.size())
      throw new IndexOutOfBoundsException("The execution index is out of range: " + executionIndex);

    // execute from ExecutionIndex
    for (; executionIndex < actions.size(); executionIndex++)
      actions.get(executionIndex).apply();
  }

  /**
   * {@inheritDoc}
   */
  public void resumeRollback()
  {
    if (executionIndex < 0 || executionIndex >= rollbackActions.size())
      throw new IndexOutOfBoundsException("The execution index is out of range: " + executionIndex);

    // execute in LIFO style (from ExecutionIndex)
    for (; executionIndex >= 0; executionIndex--)
      rollbackActions.get(executionIndex).apply();
  }

  /**
   * A list of actions, executed in a way that guarantees complete success or complete failure.
   */
  public List<Function0<Void>> getActions()
  {
    return actions;
  }

  /**
   * The index of the action that is executing. This is useful for determining the index of any actions that fail. This can be then used to
   * call the right rollback action.
   */
  public int getExecutionIndex()
  {
    return executionIndex;
  }

  /**
   * If an action fails, the corresponding rollback actions are executed
   */
  public List<Function0<Void>> getRollbackActions()
  {
    return rollbackActions;
  }

}
