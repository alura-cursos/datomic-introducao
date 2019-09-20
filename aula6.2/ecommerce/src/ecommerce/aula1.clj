(ns ecommerce.aula1
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(def conn (db/abre-conexao))

(db/cria-schema conn)

(let [computador (model/novo-produto "Computador Novo", "/computador_novo", 2500.10M)]
  (d/transact conn [computador]))

; o banco no instante que executou a linha
(def db (d/db conn))

(d/q '[:find ?entidade
       :where [?entidade :produto/nome]] db)



(let [celular (model/novo-produto "Celular Caro", "/celular", 888888.10M)]
  (d/transact conn [celular]))

; tirando uma nova fotografia (SNAPSHOT) do banco
(def db (d/db conn))

(d/q '[:find ?entidade
       :where [?entidade :produto/nome]] db)


(db/apaga-banco)





