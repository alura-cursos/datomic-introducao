(ns ecommerce.db
  (:use clojure.pprint)
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/ecommerce")

(defn abre-conexao []
  (d/create-database db-uri)
  (d/connect db-uri))

(defn apaga-banco []
  (d/delete-database db-uri))

; Produtos
; id?
; nome String 1 ==> Computador Novo
; slug String 1 ==> /computador_novo
; preco ponto flutuante 1 ==> 3500.10

; id_entidade atributo valor
; 15 :produto/nome Computador Novo     ID_TX     operacao
; 15 :produto/slug /computador_novo    ID_TX     operacao
; 15 :produto/preco 3500.10    ID_TX     operacao
; 17 :produto/nome Telefone Caro    ID_TX     operacao
; 17 :produto/slug /telefone    ID_TX     operacao
; 17 :produto/preco 8888.88    ID_TX     operacao


(def schema [{:db/ident       :produto/nome
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O nome de um produto"}
             {:db/ident       :produto/slug
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O caminho para acessar esse produto via http"}
             {:db/ident       :produto/preco
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one
              :db/doc         "O preço de um produto com precisão monetária"}])

(defn cria-schema [conn]
  (d/transact conn schema))

(defn todos-os-produtos [db]
  (d/q '[:find ?entidade
         :where [?entidade :produto/nome]] db))

; no sql eh comum fazer:
; String sql = "meu codigo sql";
; conexao.query(sql)

; esse aqui eh similar ao String sql
; eh comum e voce pode querer extrair em um def ou let
; porem... -q é notacao hungara... indica o TIPO... humm.. não parece ser legal
; em clojure
; vc vai encontrar esse padro em alguns exemplos e documentacao
; nao recomendamos notacao hungara dessa maneira, ainda menos abreviada ;)
(def todos-os-produtos-por-slug-fixo-q
  '[:find ?entidade
    :where [?entidade :produto/slug "/computador-novo"]])

(defn todos-os-produtos-por-slug-fixo [db]
  (d/q todos-os-produtos-por-slug-q db))

; não estou usando notacao hungara e extract
; eh comum no sql: String sql = "select * from where slug=::SLUG::"
; conexao.query(sql, {::SLUG:: "/computador-novo})
(defn todos-os-produtos-por-slug [db slug]
  (d/q '[:find ?entidade
         :in $ ?slug-procurado                              ; proposital diferente da variável de clojure para evitar erros
         :where [?entidade :produto/slug ?slug-procurado]]
       db slug))


; ?entity => ?entidade => ?produto => ?p
; se não vai usar... _
(defn todos-os-slugs [db]
  (d/q '[:find ?slug
         :where [_ :produto/slug ?slug]] db))

