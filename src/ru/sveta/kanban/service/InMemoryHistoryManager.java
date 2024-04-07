package ru.sveta.kanban.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import ru.sveta.kanban.task.Task;

public class InMemoryHistoryManager implements HistoryManager {

  private final HashMap<Integer, CustomNode> taskViewHistoryMap = new HashMap<>();

  private CustomNode first;
  private CustomNode last;

  @Override
  public void add(Task task) {
    removeNode(taskViewHistoryMap.get(task.getId()));
    linkLast(task);
  }

  @Override
  public List<Task> getViewHistory() {
    if (first != null) {
      List<Task> result = new ArrayList<>(taskViewHistoryMap.size());
      CustomNode node = first;
      while (node != null) {
        result.add(node.item);
        node = node.next;
      }
      return result;
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  public void remove(int taskId) {
    removeNode(taskViewHistoryMap.get(taskId));
  }

  private void removeNode(CustomNode node) {
    if (node != null) {
      taskViewHistoryMap.remove(node.item.getId());
      if (node.prev != null) {
        node.prev.next = node.next;
      } else {
        first = node.next;
      }
      if (node.next != null) {
        node.next.prev = node.prev;
      } else {
        last = node.prev;
      }
      if (taskViewHistoryMap.isEmpty()) {
        first = last = null;
      } else if (taskViewHistoryMap.size() == 1) {
        first = node.prev;
      }
    }
  }

  private void linkLast(Task task) {
    CustomNode lastNode = last;
    CustomNode newNode = new CustomNode(lastNode, task, null);
    last = newNode;
    if (lastNode == null) {
      first = newNode;
    } else {
      lastNode.next = newNode;
    }
    taskViewHistoryMap.put(task.getId(), newNode);
  }

  private static class CustomNode {

    Task item;
    CustomNode next;
    CustomNode prev;

    CustomNode(CustomNode prev, Task element, CustomNode next) {
      this.item = element;
      this.next = next;
      this.prev = prev;
    }

  }
}
