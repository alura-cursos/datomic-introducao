(ns ecommerce.aula3
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(def conn (db/abre-conexao))

(db/cria-schema conn)


(let [computador (model/novo-produto "Computador Novo", "/computador-novo", 2500.10M)
      celular (model/novo-produto "Celular Caro", "/celular", 888888.10M)
      calculadora {:produto/nome "Calculadora com 4 operações"}
      celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M)]
  (d/transact conn [computador, celular, calculadora, celular-barato]))

(pprint (db/todos-os-produtos (d/db conn)))

(pprint (db/todos-os-produtos-por-slug-fixo (d/db conn)))

(pprint (db/todos-os-produtos-por-slug (d/db conn) "/computador-novo"))

(pprint (db/todos-os-slugs (d/db conn)))


(pprint (count (db/todos-os-produtos-por-preco (d/db conn))))
(pprint (db/todos-os-produtos-por-preco (d/db conn)))

(db/apaga-banco)





