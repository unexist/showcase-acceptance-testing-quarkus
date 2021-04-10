/**
 * @package Quarkus-Kubernetes-Showcase
 *
 * @file Todo repository interface
 * @copyright 2020-2021 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the GNU GPLv3.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo.domain.todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    boolean add(Todo todo);
    boolean update(Todo todo);
    boolean deleteById(int id);
    List<Todo> getAll();
    Optional<Todo> findById(int id);
}
